# EasyRuleta

**EasyRuleta** is a Minecraft plugin designed for fun and interactive roulette mini-games. While it is primarily designed for use with the **[EasyEvent](https://github.com/skelen18/Easy-Event)** plugin for event-based roulette draws, it can be used independently without EasyEvent. The plugin allows players to spin a roulette wheel with customizable settings such as number range, blacklist, and the rotation time before the winning number is announced.

Currently, **EasyRuleta** is in **development** and may contain bugs. If you encounter any issues or have suggestions, feel free to provide feedback through the [GitHub Issues](https://github.com/skelen18/Easy-Ruletka/issues).

This plugin is available in **Czech** for the help menu, but the rest of the functionality is in **English**.

## Features

EasyRuleta offers the following commands to customize and play the roulette game:

- **Spin the Roulette:** `/ruleta spin` – Spins the roulette wheel and announces a winner.
- **Set Number Range:** `/ruleta set <min> <max>` – Sets the range of numbers for the roulette.
- **Blacklist Numbers:** `/ruleta blacklist <number>` – Adds a number to the blacklist (excluded from roulette).
- **Whitelist Numbers:** `/ruleta whitelist <number>/all` – Adds a specific number or all numbers to the whitelist (included in roulette).
- **Display All Numbers:** `/ruleta numbers` – Displays all the numbers in the range, including whitelisted and blacklisted ones.
- **Set Spin Time:** `/ruleta time <seconds>` – Sets the time for how long the roulette will spin before announcing the winner.

### Example Use Case:
After setting the number range and blacklisting a few numbers, you can spin the roulette using `/ruleta spin`. The wheel will spin for the configured time, and the winning number will be announced in a **Title** with a sound effect.

## Installation

1. Download the plugin from [releases](https://github.com/skelen18/Easy-Ruletka/releases).
2. Place the JAR file into the `plugins` folder of your Minecraft server.
3. Restart the server to load the plugin.

## EasyRuleta and EasyEvent

While **EasyRuleta** can work independently, it is designed with **EasyEvent** in mind. It can be used as an addon during an **EasyEvent** roulette-based event. The plugin can display certain design elements similar to **EasyEvent**, but it can be used standalone without the need for EasyEvent.

### Installation of EasyEvent (Optional)

- You can combine **EasyRuleta** with **EasyEvent** for a more immersive experience, especially for event-based roulette games. See the [EasyEvent GitHub repository](https://github.com/skelen18/Easy-Event) for more details.

## Language Support

The plugin interface and help commands are available in **Czech**, but the main functionality, including commands and event results, are in **English**.

## Compatibility

EasyRuleta is compatible with Minecraft versions from **1.16.5** to **1.20.1**.

## Bugs and Feedback

Since **EasyRuleta** is still under development, there may be bugs or issues. Please provide feedback and report bugs through the GitHub repository or on [Instagram](https://www.instagram.com/lordskelen18). Your feedback is important to improve the plugin!

- [GitHub Issues for EasyRuletka](https://github.com/skelen18/Easy-Ruletka/issues)

## License

This project is licensed under the [MIT License](LICENSE).

## Contact

For any questions or assistance, feel free to reach out to me on [Instagram](https://www.instagram.com/lordskelen18).
