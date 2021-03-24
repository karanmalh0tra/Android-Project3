# Android-Project3
Android Project 3 as part of the CS-478 course taught at UIC.

<h3>This is a project related to shifting between 3 applications in android.<h3>
<h5> Required the knowledge of Fragments, Permissions and Broadcast Receivers <h5>

<h4>Flow of the Project<h4>

App 1 requires a dangerous permission which is defined in the App 3.<br/>
Upon clicking a button, the user is prompted to accept or deny the App 1 of the dangerous permission.<br/>
Upon accepting, App 1's receiver is registered and an intent is passed to the App 2.

App 2 also prompts the user to accept a dangerous permission upon a button click.<br/>
If denied, the app is closed and the user is navigated back to App 1.<br/>
Upon accepting, a receiver is registered and the user moves to App 3.

App 3 defines a dangerous permission which is required by App 1 and App 2 in order to shift between apps.<br/>
App 3 displays a list of TV shows and Images of characters from that TV Show.<br/>
App 3 utilizes fragments to view the shows and characters in portrait and landscape mode.<br/>
Upon clicking on options menu, the App 3 sends a broadcast to App 1 and App 2.<br/>
Upon receiving a broadcast, App 2 displays a toast message whereas App 1 displays a web view with a Wikipedia URL of that TV Show.
