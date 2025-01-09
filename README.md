# yandex-pay-demo-android

This is a demo app for the Yandex Pay SDK library for Android. It demonstrates the usage of the Yandex Pay SDK Inventory for rendering widgets and also provides an example for starting the checkout process and obtaining the checkout result.

# Using the demo app:

You can use this demo app as a reference point for your merchant app with the following modifications:

- replace the sandbox merchant ID with your own merchant ID;

- replace the sandbox API key with your own API key. Please note that the API key for the sandbox merchant is the same as its merchant ID, but that's only true for the sandbox. You should provide both your merchant ID and your API key;

- replace the YANDEX_CLIENT_ID placeholder in the build.gradle.kts file with your proper YANDEX_CLIENT_ID. The correct client ID is required for propagating the checkout result back into your merchant app;

- use your own certificate to sign the app. The demo app uses a certificate that is downloaded before the gradle build. It should not be used anywhere else.

Please refer to the official documentation for more information about registering your own merchant ID, API key and client ID: https://pay.yandex.ru/docs/ru/custom/android-sdk/url-flow

# More information:

Yandex Pay SDK for Android overview: https://pay.yandex.ru/docs/ru/custom/android-sdk/

Yandex Pay SDK for Android changelog: https://pay.yandex.ru/docs/ru/custom/android-sdk/changelog-bolt

Yandex Pay SDK Inventory changelog: https://pay.yandex.ru/docs/ru/custom/android-sdk/inventory/changelog-inventory
