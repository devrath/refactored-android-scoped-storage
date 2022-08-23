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



| **` Contents of Wiki `** |
| ------------------------ |
| [**` Problems associated with external storage `** ](https://github.com/devrath/refactored-android-scoped-storage/wiki/Problems-associated-with-external-storage) |
| [**` How scoped storage is a solution to external storage drawbacks `** ](https://github.com/devrath/refactored-android-scoped-storage/wiki/How-scoped-storage-is-a-solution-to-external-storage-drawbacks) |
| [**` Flow diagram representation of storing data `** ](https://github.com/devrath/refactored-android-scoped-storage/wiki/Flow-diagram-representation-of-storing-data) |

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
