# FoodGram

## Table of Contents
1. [Overview](#Overview)
1. [Product Spec](#Product-Spec)
1. [Wireframes](#Wireframes)
2. [Schema](#Schema)

## Overview
### Description
Individuals choose the food store they find awesome to follow and view the food store’s profile which includes food pictures, price, and comments. Individuals share their consumption experience in any kind of food store as the instagram-like posts. They can also see their friends' posts to decide which restaurants to try in the future.

### App Evaluation

- **Category:** Social Networking / Food
- **Mobile:** This app would be primarily developed for mobile but would perhaps be just as viable on a computer, such as Instagram or other similar apps. Functionality wouldn’t be limited to mobile devices, however mobile version could potentially have more features.
- **Story:** Provide a platform of sharing food information from the users and the food stores.
- **Market:** Any individual could choose to use this app, and to keep it a safe environment, people would be organized into age groups.
- **Habit:** This app could be used as often or un-often as the user wanted depending on how deep their social life is, and what exactly they’re looking for.
- **Scope:** First we would start with pairing people based on music taste, then perhaps this could evolve into a music sharing application as well to broaden its usage. Large potential for use with spotify, apple music, or other music streaming applications.

## Product Spec

### 1. User Stories (Required and Optional)

**Required Must-have Stories**

* Users sign up as a normal user or a food store.
* Normal User logs in to access friend list, previous chats, and list of following food stores.
* Normal User follow what they are interested in. 
* Users who are friends have a chat window to communicate.
* Each normal user has a profile page which shows the posts submitted. Friends can thums up or add comments on each post.
* Each normal user can view the profile of the food store that they followed.
* Food store users log in to edit their profiles, such as edit the food pictures, prices, etc.
* Settings (Accesibility, Notification, General, etc.)

**Optional Nice-to-have Stories**

* The background basically is a map (like google map). And it is able to locate the user’s own position
* Users are able to create an invitation or see the current existing invitation on the map. (An invitation is to create a team to have food together)
* When creating the invitation, the user has to set up the number of people to join, some words to say on invitation, and the place of the restaurant. 
* These invitations will appear on the map as a small icon. The icon will contain the words the creator wrote, and the headshot of users who are currently on the team. 
* When a user tries to join a team, they click the small icon on the map to apply.
* When a user clicks the small icon on the map, a new intent appears. In this new intent:
* The user can see the details of the invitation information:
* The information of the users who are currently in the team. Such the their username, headshot photo, gender, age, etc
* The words that the creator worte.
* Two buttons: Apply and Cancel. 
* The invitation creator is able to cancel or start the team meeting. 
* Starting the team meeting, the system will add all people on the team to a group chat.

### 2. Screen Archetypes

* Normal Users
   * Login
   * Register - User signs up or logs into their account
      * Upon Download/Reopening of the application, the user is prompted to log in to gain access to their profile information to be properly matched with another person.
      * Users will choose to sign up as normal users or food store users.
   * Normal User main page (view followed food store posts)
      * Show followed food store posts chronologically. 
      * Each post has the user’s headshot, food picture, created time and some words.
      * Click each post to see the details which include larger pictures, comments.
  * Normal User second page (view friend's posts)
      * Show all friend’s posts chronologically. 
      * Each post has the user’s headshot, food picture, and some words.
      * Click each post to see the details which include larger pictures, comments.
      * Tap on top right corner of menu bar allow user to create a new post
  * Users third page(optional)
      * Showing a list of contents, includes: Find location, Notification, Temporary Message
  * Normal User fourth page
      * Show the list of friends, in a recyclerview
      * Each item in recyclerview contains the user’s headshot, username, and the last message sent.
      * Items are ordered chronologically.
      * On the top bar of the page, shows the number of unread messages. 
  * Normal User fifth page (User’s own profile)
      * Shows the user’s headshot icon and username, total number of posts made, number of food stores followed, and number of friends added.
      * Show a grid view of the most recent nine photos
      * Show all posts from the past in recyclerview contain posted time, description.Tap on these posts to show a detailed view.
      * Show friends and followed list
      * Settings Screen
      * Lets people change language, and app notification settings.

* Food Store Users
   * Food Store User main page (view followed food store posts)
      * Show followed food store posts chronologically. 
      * Each post has the user’s headshot, food picture, created time and some words.
      * Click each post to see the details which include larger pictures, comments.
      * Tap on top right corner of menu bar allow food store user to create a new post
   * Food Store User second page (view friend's posts)
      * Show all friend’s posts chronologically. 
      * Each post has the user’s headshot, food picture, and some words.
      * Click each post to see the details which include larger pictures, comments.
   * Food Store User third page
      * Show the list of friends, in a recyclerview
      * Each item in recyclerview contains the user’s headshot, username, and the last message sent.
      * Items are ordered chronologically.
      * On the top bar of the page, shows the number of unread messages. 
   * Food Store User fourth page (own profile)
      * Shows the user’s headshot icon and username, total number of posts made, number of followers, and number of friends added.
      * Show menu in recyclerview contains food image, food name, price
      * Show all posts from the past in recyclerview contain posted time, description. Tap on these posts to show a detailed view.
      * Show friends and followed list

### 3. Navigation

Optional:
Music/Encounter Queue
Discover (Top Choices)
**Tab Navigation** (Tab to Screen)

* Music selection
* Profile
* Settings
* Optional:
   * Music/Encounter Queue
   * Discover (Top Choices)

**Flow Navigation** (Screen to Screen)

* Forced Log-in
   * Account creation if no login is available
* Music Selection (Or Queue if Optional)
   * Jumps to Chat
* Profile
   * Text field to be modified.
* Settings Toggle settings

## Wireframes
[Add picture of your hand sketched wireframes in this section]
<img src="YOUR_WIREFRAME_IMAGE_URL" width=600>

### [BONUS] Digital Wireframes & Mockups

### [BONUS] Interactive Prototype

### Networking
- [Add list of network requests by screen ]
- [Create basic snippets for each Parse network request]
- [OPTIONAL: List endpoints if using existing API such as Yelp]
