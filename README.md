# GreeControl - Android
> DISCLAIMER: This project is a fork of [tomikaa87's GreeRemoteAndroid](https://github.com/tomikaa87/gree-remote/tree/master/GreeRemoteAndroid) demo application.

*This fork started back in 2020, and used it ever since, until recently, when I discovered, that it was updated with Wi-Fi capability, so I had to upgrade it, but I thought that someone else might also like to use my version of it, so I uploaded it to GitHub.*

## How does it work?
If you're interested how the protocol works, you can find that out [here](https://github.com/tomikaa87/gree-remote/blob/master/README.md#protocol-details).

Currently, the application is directly communicating with the ACs. I plan to create a HUB, that can sit on your network (possibly written in C#), and be a middleware between your phone and the ACs themselves.
I'll make possible to control your ACs from your phone, desktop and maybe from remotely.

It will be similar to Ubiquiti's control panel, where you can manage your UniFi switches, APs etc. from a centralized web interface (and also from your phone using their dedicated app).

## Where you can find the app?
You can download this app from the releases page in GitHub. You can build this app yourself to an APK and install it on your phone.
I'll share detailed steps in the future.
Until then, you can find it in `IntelliJ` -> `Gradle` -> `Tasks` -> `build` -> `assemble` or by running `gradlew assemble` in the project root folder.

## License
Because of the nature of this project (a fork of `GreeRemoteAndroid`) it derives the parent project's [license](LICENSE), which is included in the root of this repo.
