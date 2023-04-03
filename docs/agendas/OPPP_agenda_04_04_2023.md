# Meeting Agenda
Date:           04-04-2023\
Main focus:     Concluding the product\
Chair:          Rares Bites\
Note taker:     Tudor Tanasescu

## Opening (~1 min)
How is everyone doing?

## Approval of the agenda (~1 min)
Is there something that we need to discuss not in the agenda yet?

## Product demonstration (~3 min)
- Show what is new from last week.
- Is there still something to be done?

## TA announcements (~3 min)

## Agenda items (~5 min)
- Deadlines
  - Re-doing the heuristic evaluation for a better grade (April 7)
  - Proudct pitch meeting on Friday. When are you available that day, so we can book a project room? (April 7)
  - Repository turns read-only this Friday. Per team what is their progress.
- This week's focus. For each of them, we're gonna ask who does what:
  - Incorporating web sockets and long polling into at least one of our features. This can be done by taking an hour to watch the self-study and implement them as shown there.
  - Resolving all bugs/things that still need to be added to make sure the entire app is consistent and in conformity with all backlog requirements. Specifically:
    - Add alerts before deleting cards, boards & closing the entire app (Are you sure you want to ……)
    - Force the titles of the cards, boards, and server address not be null through the UI and to have at most N characters, depending on how many characters can be visible on the screen (the number “N” is taken from the database constraints)
    - Add (read-only, just UI) trash icons to every delete button, add pencil icon to edit buttons, add door icons to leave buttons, etc. —> increases recognition —> better user design according to our heuristics
    - Make it possible to update text by pressing enter instead of only by clicking the corresponding buttons
    - Backspace doesn’t work in quick add card
    - Leave doesn’t work after adding a card or a list
  - All of Khalid's "meeting"/"process" related feedback needs to be taken into consideration (for example: all agendas and minutes need to be made in the same format -- either all PDFs or all MDs -- for consistency, etc.).

## Last check (~1 min)
Anything anyone would still like to add?

## Summary (~1 min)
I will kindly ask the team if they agree to skip this section and directly do to the questions part, since we have limited time. :)

## Questions for the TA (~9 min)
- How can we ask Sebastian to take a look at our app? He mentioned he is always happy to do so. :)
- Product pitch: can we “show” all the features in the video but only “talk” about what we did better and unique than the backlog requirements?
- Backlog clarifications: Whether closed and reopened clients need to keep the user data locally and reload them when reconnecting.
- Testing: coverage is wanted to be 80% in the grading rubric. However, there is a lot of JavaFX in our app and this can be seen considering the design is very fluid. If we had only implemented the functionality and didn’t care about the user’s perspective, then obviously 80% coverage would have been attainable. But otherwise, since Sebastian specifically mentioned that only services in the client can be tested (which we did), is our app going to receive a full mark for testing? Are manual testing plans just as good?
- Heuristic usability evaluation:
  - What did you mean that it is not replicable? We described it exactly as seen in the lecture and in the article. What would you suggest we improve? Would a form of how easy/difficult it is to add boards/cards etc. be fine?
  - Would you be open to take a quick look on our updated heuristic usability evaluation after we finish writing it? Just to make sure that we improved on the points you made.
- Other questions if time allows it.

## Feedback round (~1 min)
What went well and what can still be improved?

## Closing
Close meeting.
