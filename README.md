<img src="https://github.com/devrath/devrath/blob/master/images/kotlin_logo.png" align="right" title="Kotlin Logo" width="120">

# refactored-android-scoped-storage 🧞‍


<p align="left">
<a><img src="https://img.shields.io/badge/Language-kotlin-lightgrey"></a>
<a><img src="https://img.shields.io/badge/Threading-coroutines-red"></a>
<a><img src="https://img.shields.io/badge/operations-file--handeling-green"></a>
<a><img src="https://img.shields.io/badge/Storage-scoped--storage-pink"></a>
</p>

## `𝚆𝚑𝚊𝚝 𝚒𝚜 𝚊 𝚂𝚌𝚘𝚙𝚎𝚍 𝚂𝚝𝚘𝚛𝚊𝚐𝚎`💡
🏷️ We are aware that working with a file system is a very important part of android development. Till the android `version-10`, once the user gave permission to access the external storage, he could access any file on the device.</br>
🏷️ But most of the time we don't need the entire storage system to be accessed. Users mostly do some action on a file or a group of files and don't need access to the entire file system, due to this there was a concern of security and privacy of the user.</br>
🏷️ The scoped storage enhances the control of user privacy on the device.

## `𝚆𝚑𝚊𝚝 𝚒𝚜 𝚊 𝙵𝚒𝚕𝚎 𝚂𝚢𝚜𝚝𝚎𝚖`
### `𝙳𝚎𝚏𝚒𝚗𝚒𝚝𝚒𝚘𝚗`:
* `FileSystem` is a way of storing data in `documents` instead of `key-value` mapping mechanism.
* Using this mechanism, we can `fetch`, `add`, `delete`, and move it to a different location.
* We use it to store large sizes of data like `images`, `audio`, `video`, and so on.
* We can store the data here instead of `shared preferences` or `local database`.

### `𝙷𝚘𝚠 𝚝𝚘 𝚞𝚜𝚎 𝚝𝚑𝚎 𝚏𝚒𝚕𝚎 𝚜𝚢𝚜𝚝𝚎𝚖`
* We need to get access to the `root-file` or `root-directory`.
* Once we have the access, we can add more children files within the accessed location.
* The file is created with a name using the stream

### `𝙷𝚘𝚠 𝚝𝚑𝚎 𝚜𝚝𝚛𝚎𝚊𝚖𝚜 𝚠𝚘𝚛𝚔`

<p align="center">
  <img src="https://github.com/devrath/refactored-android-scoped-storage/blob/main/assets/Untitled%20Diagram.png">
</p>


* Stream is a pipe that can `send data in one direction`, or `receive the data from the other direction`.
* We have two types of streams an `input stream` and an `output stream`.
  * `Input streams` ---> Used for reading data from a file.
  * `Output streams` --> Used for writing data to a file.
* We get a reference to a file or directory, Then Using that obtained reference create an `input stream` or the `output stream`. Then using the particular stream we modify or read the data.

## `𝚃𝚢𝚙𝚎𝚜 𝚘𝚏 𝚂𝚝𝚘𝚛𝚊𝚐𝚎𝚜 𝚒𝚗 𝙰𝚗𝚍𝚛𝚘𝚒𝚍`
<p align="center">
  <img src="https://github.com/devrath/refactored-android-scoped-storage/blob/main/assets/typesOfStorage.png">
</p>

### `𝙸𝚗𝚝𝚎𝚛𝚗𝚊𝚕 𝚂𝚝𝚘𝚛𝚊𝚐𝚎`
* Each application has a `private directory`.
* Thus many applications have their own `individual private directories`
* The private directory of one application is not visible to the other application.
* This is the easiest way of storing the data 
* Permission is not required to store the data in the internal storage

### `𝙴𝚡𝚝𝚎𝚛𝚗𝚊𝚕 𝚂𝚝𝚘𝚛𝚊𝚐𝚎`
* Everything else apart from the internal storage is considered as external storage.
* Permission is required for storing data in external storage.
* Once you store the data(ex:-image) in the external storage, all the apps can access this data.
* But all the images stored in external storage are tracked by the apps that created it.
* Say `application-1` creates one image in the external storage, and the `application-2` cannot delete it directly, instead requires the permission of the user before deleting it.

## `𝙿𝚛𝚘𝚋𝚕𝚎𝚖𝚜 𝚊𝚜𝚜𝚘𝚌𝚒𝚊𝚝𝚎𝚍 𝚠𝚒𝚝𝚑 𝚎𝚡𝚝𝚎𝚛𝚗𝚊𝚕 𝚜𝚝𝚘𝚛𝚊𝚐𝚎`
* Lot of applications need permission.
```xml
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"/>
``` 
* Basically anyone with the above permission can access the files from external storage. Describing more like say We build an app and we can access all the images stored in external storage using above permission
* But most of them do only simple things with it. One example of this is you want to access `photos` in `external storage`, but you might not need `pdf`, `doc` etc. 
* They might not have needed to access the whole storage but earlier they could.
* The files are left in external storage sometimes and occupy space.
* Even though the application is un-installed still the files are left as it is in the external storage.

## `𝙷𝚘𝚠 𝚜𝚌𝚘𝚙𝚎𝚍 𝚜𝚝𝚘𝚛𝚊𝚐𝚎 𝚒𝚜 𝚊 𝚜𝚘𝚕𝚞𝚝𝚒𝚘𝚗 𝚝𝚘 𝚎𝚡𝚝𝚎𝚛𝚗𝚊𝚕 𝚜𝚝𝚘𝚛𝚊𝚐𝚎 𝚍𝚛𝚊𝚠𝚋𝚊𝚌𝚔𝚜`
<p align="center">
<h4>Scope storage was introduced to increase user privacy and give the end-user the decision making step to control their privacy</h2>
</p>

* Scoped storage was a solution to the problem. 
* Scoped storage is optional in **`android-10`** but has become compulsory in **`android-11`**.
* Because of scoped storage, System knows which application has created which file and has a track of each file, Thus when an application is uninstalled all the files that it created are also un-installed.
* Every application has access to its **`directory`** in `external storage` and **`does not need permission`**.

### `𝙼𝚘𝚍𝚒𝚏𝚢𝚒𝚗𝚐 𝚏𝚒𝚕𝚎𝚜 𝚒𝚗 𝚎𝚡𝚝𝚎𝚛𝚗𝚊𝚕 𝚜𝚝𝚘𝚛𝚊𝚐𝚎 𝚌𝚛𝚎𝚊𝚝𝚎𝚍 𝚏𝚛𝚘𝚖 𝚘𝚝𝚑𝚎𝚛 𝚊𝚙𝚙𝚕𝚒𝚌𝚊𝚝𝚒𝚘𝚗𝚜?`
<p align="left">
  <img width=300 width=200 src="https://github.com/devrath/refactored-android-scoped-storage/blob/main/assets/files.gif">
</p>

* We can perform this action using **`createWriteRequest`**, **`createDeleteRequest`** and modify the files in the directory created by other applications in `external storage`
* But the catch here is, that it requires the approval of the user.

### `𝙳𝚎𝚕𝚎𝚝𝚒𝚗𝚐 𝚝𝚑𝚎 𝚏𝚒𝚕𝚎𝚜`
* We can now `move the files to the trash` instead of `deleting them`.
* We can recover the items from the trash in a `30-days` life span.

### `𝚂𝚙𝚎𝚌𝚒𝚊𝚕 𝚊𝚙𝚙𝚕𝚒𝚌𝚊𝚝𝚒𝚘𝚗𝚜 𝚝𝚑𝚊𝚝 𝚒𝚗𝚟𝚘𝚕𝚟𝚎 𝚏𝚒𝚕𝚎 𝚑𝚊𝚗𝚍𝚕𝚒𝚗𝚐`
* Some of the applications that are just developed to handle the files themselves need access to the entire file system because that's the primary purpose of the application. How will that get handled is using special permission called.
```kotlin
<uses-permission android:name="android.permission.MANAGE_EXTERNAL_STORAGE"/>
```
* If we add this permission, we do get access to the entire `file folder`. 
* But to add this to the play store, we need to follow a review process and approval from google justifying why this permission is required.
* After all the process, like any other permission, end users can disable the permission in the settings.

## `𝙵𝚕𝚘𝚠 𝚍𝚒𝚊𝚐𝚛𝚊𝚖 𝚛𝚎𝚙𝚛𝚎𝚜𝚎𝚗𝚝𝚊𝚝𝚒𝚘𝚗 𝚘𝚏 𝚜𝚝𝚘𝚛𝚒𝚗𝚐 𝚍𝚊𝚝𝚊`
---
<p align="center">
  <img src="https://github.com/devrath/refactored-android-scoped-storage/blob/main/assets/ScopeStorage.png">
</p>

## **`𝙲𝚘𝚗𝚝𝚛𝚒𝚋𝚞𝚝𝚎`** 🙋‍♂️
Read [contribution guidelines](CONTRIBUTING.md) for more information regarding contribution.

## **`𝙵𝚎𝚎𝚍𝚋𝚊𝚌𝚔`** ✍️ 
Feature requests are always welcome, [File an issue here](https://github.com/devrath/refactored-android-scoped-storage/issues/new).

## **`𝙵𝚒𝚗𝚍 𝚝𝚑𝚒𝚜 𝚙𝚛𝚘𝚓𝚎𝚌𝚝 𝚞𝚜𝚎𝚏𝚞𝚕`** ? ❤️
Support it by clicking the ⭐ button on the upper right of this page. ✌️

## **`𝙻𝚒𝚌𝚎𝚗𝚜𝚎`** ![Licence](https://img.shields.io/github/license/google/docsy) :credit_card:
This project is licensed under the Apache License 2.0 - see the [LICENSE](https://github.com/devrath/refactored-android-scoped-storage/blob/main/LICENSE) file for details


<p align="center">
<a><img src="https://forthebadge.com/images/badges/built-for-android.svg"></a>
</p>
