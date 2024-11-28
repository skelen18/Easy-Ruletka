package skelen.easyRuletka;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

public final class EasyRuletka extends JavaPlugin {

    private int minNumber = 0;
    private int maxNumber = 100;
    private int spinTime = 10;
    private Set<Integer> blacklist = new TreeSet<>();
    private Set<Integer> currentNumbers = new TreeSet<>();
    private Random random = new Random();

    @Override
    public void onEnable() {
        getLogger().info("EasyRuleta plugin has been enabled!");
        initializeNumbers();
        getCommand("ruleta").setTabCompleter(new RuletaTabCompleter());
    }

    @Override
    public void onDisable() {
        getLogger().info("EasyRuleta plugin has been disabled!");
    }

    private void initializeNumbers() {
        currentNumbers.clear();
        for (int i = minNumber; i <= maxNumber; i++) {
            if (!blacklist.contains(i)) {
                currentNumbers.add(i);
            }
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("ruleta")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(ChatColor.RED + "This command can only be used by players.");
                return true;
            }

            Player player = (Player) sender;

            if (!player.isOp()) {
                player.sendMessage(ChatColor.RED + "You do not have permission to use this command.");
                return true;
            }

            if (args.length == 0) {
                displayHelp(sender);
                return true;
            }

            switch (args[0].toLowerCase()) {
                case "spin":
                    if (args.length == 1) {
                        spinRuleta(player);
                    } else {
                        sender.sendMessage(ChatColor.RED + "Usage: /ruleta spin");
                    }
                    return true;

                case "set":
                    if (args.length == 3) {
                        try {
                            int min = Integer.parseInt(args[1]);
                            int max = Integer.parseInt(args[2]);
                            if (min >= 0 && max <= 100 && min < max) {
                                minNumber = min;
                                maxNumber = max;
                                initializeNumbers();
                                sender.sendMessage(ChatColor.GREEN + "Ruleta range set to " + min + " - " + max + ".");
                            } else {
                                sender.sendMessage(ChatColor.RED + "Invalid range. Please use numbers between 0 and 100.");
                            }
                        } catch (NumberFormatException e) {
                            sender.sendMessage(ChatColor.RED + "Invalid number format.");
                        }
                    } else {
                        sender.sendMessage(ChatColor.RED + "Usage: /ruleta set <min> <max>");
                    }
                    return true;

                case "blacklist":
                    if (args.length == 2) {
                        try {
                            int number = Integer.parseInt(args[1]);
                            if (currentNumbers.contains(number) && currentNumbers.size() > 2) {
                                blacklist.add(number);
                                currentNumbers.remove(number);
                                sender.sendMessage(ChatColor.RED + "Number " + number + " added to blacklist.");
                            } else {
                                sender.sendMessage(ChatColor.RED + "Cannot blacklist this number.");
                            }
                        } catch (NumberFormatException e) {
                            sender.sendMessage(ChatColor.RED + "Invalid number format.");
                        }
                    } else {
                        sender.sendMessage(ChatColor.RED + "Usage: /ruleta blacklist <number>");
                    }
                    return true;

                case "whitelist":
                    if (args.length == 2) {
                        if (args[1].equalsIgnoreCase("all")) {
                            currentNumbers.addAll(blacklist);
                            blacklist.clear();
                            sender.sendMessage(ChatColor.GREEN + "All numbers removed from blacklist.");
                        } else {
                            try {
                                int number = Integer.parseInt(args[1]);
                                if (blacklist.contains(number)) {
                                    blacklist.remove(number);
                                    currentNumbers.add(number);
                                    sender.sendMessage(ChatColor.GREEN + "Number " + number + " removed from blacklist.");
                                } else {
                                    sender.sendMessage(ChatColor.RED + "This number is not in the blacklist.");
                                }
                            } catch (NumberFormatException e) {
                                sender.sendMessage(ChatColor.RED + "Invalid number format.");
                            }
                        }
                    } else {
                        sender.sendMessage(ChatColor.RED + "Usage: /ruleta whitelist <number>/all");
                    }
                    return true;

                case "numbers":
                    displayNumbers(sender);
                    return true;

                case "time":
                    if (args.length == 2) {
                        try {
                            int time = Integer.parseInt(args[1]);
                            if (time >= 5 && time <= 20) {
                                spinTime = time;
                                sender.sendMessage(ChatColor.GREEN + "Spin time set to " + time + " seconds.");
                            } else {
                                sender.sendMessage(ChatColor.RED + "Time must be between 5 and 20 seconds.");
                            }
                        } catch (NumberFormatException e) {
                            sender.sendMessage(ChatColor.RED + "Invalid number format.");
                        }
                    } else {
                        sender.sendMessage(ChatColor.RED + "Usage: /ruleta time <seconds>");
                    }
                    return true;

                default:
                    displayHelp(sender);
                    return true;
            }
        }
        return false;
    }

    private void spinRuleta(Player player) {
        if (currentNumbers.size() < 2) {
            player.sendMessage(ChatColor.RED + "There must be at least 2 numbers available to spin.");
            return;
        }

        final int totalTicks = spinTime * 20;
        final int intervalTicks = 3;

        new BukkitRunnable() {
            int elapsedTicks = 0;

            @Override
            public void run() {
                if (elapsedTicks >= totalTicks) {
                    displayFinalNumber();
                    cancel();
                    return;
                }

                int selected = getRandomNumber();
                String title = ChatColor.YELLOW + "Ruleta: " + formatNumber(selected);
                String subtitle = ChatColor.DARK_GRAY + "Spinning...";

                sendTitleToAllPlayers(title, subtitle, 0, 5, 1);

                playSoundForAllPlayers(Sound.BLOCK_NOTE_BLOCK_BELL, 1.0f, 1.0f);

                elapsedTicks += intervalTicks;
            }

            private void displayFinalNumber() {

                int finalNumber = getRandomNumber();
                String title = ChatColor.YELLOW + "Ruleta: " + formatNumber(finalNumber);
                String subtitle = "";
                sendTitleToAllPlayers(title, subtitle, 0, 60, 20);

                Bukkit.broadcastMessage(ChatColor.GOLD + "Ruleta selected number: " + formatNumber(finalNumber));
                playSoundForAllPlayers(Sound.BLOCK_NOTE_BLOCK_BELL, 1.0f, 1.5f);

                new BukkitRunnable() {
                    @Override
                    public void run() {
                        sendTitleToAllPlayers("", "", 0, 5, 1);
                    }
                }.runTaskLater(EasyRuletka.this, 60L);
            }

            private int getRandomNumber() {
                List<Integer> numbersList = new ArrayList<>(currentNumbers);
                return numbersList.get(random.nextInt(numbersList.size()));
            }
        }.runTaskTimer(this, 0L, intervalTicks);
    }

    private void sendTitleToAllPlayers(String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendTitle(title, subtitle, fadeIn, stay, fadeOut);
        }
    }

    private void playSoundForAllPlayers(Sound sound, float volume, float pitch) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.playSound(player.getLocation(), sound, volume, pitch);
        }
    }

    private String formatNumber(int number) {
        if (number == 0) {
            return ChatColor.GREEN + "0";
        } else if (number % 2 == 0) {
            return ChatColor.RED + String.valueOf(number);
        } else {
            return ChatColor.DARK_GRAY + String.valueOf(number);
        }
    }

    private void displayHelp(CommandSender sender) {
        sender.sendMessage(ChatColor.GREEN + "---------- EasyRuleta Commandy ----------");
        sender.sendMessage(ChatColor.YELLOW + "/ruleta spin" + ChatColor.WHITE + " - Roztočí ruletu.");
        sender.sendMessage(ChatColor.YELLOW + "/ruleta set <min> <max>" + ChatColor.WHITE + " - Nastavení rozsahu čísel.");
        sender.sendMessage(ChatColor.YELLOW + "/ruleta blacklist <number>" + ChatColor.WHITE + " - Přidat číslo na blacklist.");
        sender.sendMessage(ChatColor.YELLOW + "/ruleta whitelist <number>/all" + ChatColor.WHITE + " - Přidat číslo/all na whitelist.");
        sender.sendMessage(ChatColor.YELLOW + "/ruleta numbers" + ChatColor.WHITE + " - Zobrazí to všechna čísla.");
        sender.sendMessage(ChatColor.YELLOW + "/ruleta time <seconds>" + ChatColor.WHITE + " - Nastavení doby spinování.");
    }

    private void displayNumbers(CommandSender sender) {
        StringBuilder numbers = new StringBuilder();
        StringBuilder blacklistedNumbers = new StringBuilder();

        for (int number : currentNumbers) {
            numbers.append(formatNumber(number)).append(" ");
        }

        for (int number : blacklist) {
            blacklistedNumbers.append(ChatColor.DARK_RED).append(number).append(" ");
        }

        sender.sendMessage(ChatColor.GOLD + "Current numbers: " + numbers.toString());
        if (blacklistedNumbers.length() > 0) {
            sender.sendMessage(ChatColor.GOLD + "Blacklisted numbers: " + blacklistedNumbers.toString());
        }
    }

    private static class RuletaTabCompleter implements TabCompleter {
        @Override
        public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
            if (command.getName().equalsIgnoreCase("ruleta")) {
                if (args.length == 1) {
                    return getTabCompletions(args[0], "spin", "set", "blacklist", "whitelist", "numbers", "time");
                } else if (args.length == 2) {
                    if (args[0].equalsIgnoreCase("time")) {
                        List<String> completions = new ArrayList<>();
                        for (int i = 5; i <= 20; i++) {
                            completions.add(String.valueOf(i));
                        }
                        return completions;
                    }
                }
            }
            return null;
        }

        private List<String> getTabCompletions(String currentArg, String... options) {
            List<String> completions = new ArrayList<>();
            for (String option : options) {
                if (option.startsWith(currentArg.toLowerCase())) {
                    completions.add(option);
                }
            }
            return completions;
        }
    }
}
