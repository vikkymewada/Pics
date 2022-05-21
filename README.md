# Pics Photo Application
Photo's are fetched from picsum endpoints.

## Features
1. Splash Screen
   1. As per android 12
2. Showing a list of images fetched from picsum endpoint.
    1. Transparent theme 
    2. Shared Element Transition to Image Detail screen
    3. Infinite Scroll 
    4. Device rotation 
    5. Device/Tablet - Number of image column increases on tablet
3. Image Detail screen 
   1. Full resolution image 
   2. Blurred background effect for immersive user experience 
   3. Author Details 
   4. Pinch to Zoom 
   5. Share 
   6. Save 
   7. Closing as per support provided by shared element transition 
   
    
# Technical Details 
1. Recyclerview with PagerAdapter 
2. Room
3. RemoteMediator for handling paging 
4. Snackbar 
5. Network connectivity checks 
6. Retry option on paging adapter 
7. Hilt 
8. MVVM arch 

# Third party Libraries
1. Retrofit - Networking 
2. Okhttp - Networking 
3. Glide - Image caching/downloading/rendering 
4. zoomage - Pinch to Zoom imageview
. 

# Note 
Jetpack compose does not support shared element transition as of 21st May 2022 and hence not developed with jetpack compose.

# Future plans 
Test cases (junit + UI automation)

Add support for 
    - Searching images based on tags 
    - Random image load 
    - Image from same author / related images 

Report image 
    - Copyright/Other issues

Settings 
    - Control over caching/memory
    - Help / Support 
Detail Screen 
    - Identify if already saved. 
    - Info on camera, lenses, date




https://user-images.githubusercontent.com/2221052/169649770-e078150f-db8a-4f7c-a421-c200768b02fd.mp4


![pics_home_screen](https://user-images.githubusercontent.com/2221052/169649702-c78f38f0-52d9-4e30-ba5f-d2431f7085c7.png)
![pics_home_screen_tablet](https://user-images.githubusercontent.com/2221052/169649709-35c7f207-c7f0-404f-8f5c-27894bfaaff6.png)
![pics_detail_screen](https://user-images.githubusercontent.com/2221052/169649733-ca01b0ac-f3e7-43e9-bdc4-cb87721e7842.png)



