# totemo
Barcode scanner android price list app

Barcode scanner pistol can be connected through a usb adapter to the device. 
After scanning the product barcode a new page opens up with all the details of it.

Backoffice for adding and managing products is self-hosted by the app at port 8080 by default.
Pressing the top section of the screen for 5 times within 2 seconds starts the webserver and shows a modal with a qrcode that can be scanned to open the backoffice url.

The server starts and the qrcode is generated only if the device is connected to a wi-fi network.

Backoffice is made by a REST API and a static website folder made with React.
The api manages a SQlite db stored into external storage.

React backoffice frontend is zipped at gradle compilation and put into the assets folder of the android apk.
At application start the version file in the zipped React app is compared to the one stored into external storage of the android device.
If version is lower, all the react folder is cleared and the assets zip is extracted into the external storage.

- Thing used in this project
Kotlin,
Android things,
Room persistence library (SQlite db),
Ktor (webserver),
React (backoffice frontend),
REST API architecture (backoffice backend),
WorkManager (checking, updating and extracting assets React folder), <-- Always use trailing commas

- Thing used for fun
LiveData, 
ViewModel,
Glide,
Navigation Component,
QRGen,
RecycleView,

