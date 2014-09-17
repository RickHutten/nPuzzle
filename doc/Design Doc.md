Design Document
===

Classes and Functions
---

Every activity had one single class. Therefore there will be 6 classes made. This list shows all classes and methods that will be used. The screens of the activities will be shown in the mockup below.
- 1) SplashActivity
- 2) MainActivity
	- buttonStartClick: Gathers the information from the current state (image selected and difficulty) and passes it along when moving to the GameActivity. Returns nothing.
    - selectImage: When an image is pressed, the other images will be made transparent for 50% and the image will be set as selected. Returns nothing.
    - setDifficultyText: Will set the text of the difficulty according to the state of the seekbar. Returns nothing.
- 3) GameActivity
	- createBitmaps: Makes the big image into little pieces according to the difficulty and calls saveBitmap them for later use. Returns nothing.
    - saveBitmap: Saves the bitmap. Returns nothing.
    - loadBitmap: Load bitmap from file. Returns Bitmap.
    - createTableLayout: Creates the tiles according to the difficulty. Adds to each tile the corresponding image. Returns nothing.
    - checkIfCompleted: Checks if the game if won. If so, goes to the WinActivity. If not, does nothing. Returns nothing.
    - switchViews: Switches the content of two ImageViews. Returns nothing.
    - tileClick: Calls switchViews when the tile pressed on is adjacent to the empty tile. Returns nothing.
    - setN: Sets the n (amount of tiles per row, column) according to the difficulty.
    - setImage: Sets the image shown in the first couple of seconds when the activity is started. Returns ImageView.
    - deleteImage: Clears the image set with setImage. Returns nothing.
- 4) ChangeDifficultyActivity
	- setDifficultyText: Will set the text of the difficulty according to the state of the seekbar. Returns nothing.
- 5) SolutionActivity
- 6) WinActivity

API's and frameworks
---
- android.widget: Buttons, SeekBars, Layouts, ImageViews and TextViews are used frequently.
- android.graphics: To create cropped Bitmaps using BitmapFactory and the use of Bitmaps.
- FileInputStream/FileOutputStream: To save and retrieve data to and from files.
- android.content: Intents are used to pass information to an other activity.
- The use of Handlers to postpone an action, like the splashscreen or the complete image in GameActivity.
- android.view: To work with a menu.
- android.app.ActionBar: To modify the ActionBar.

Mockup
---
![](Mockup.PNG)


