# UI Test Plan

This is the source of truth for Bob's console UI tests. Cases run in order, each in a fresh process.

## Test configuration

- **Java version:** 25 (`sdk use java 25.0.3.fx-zulu` on macOS when SDKMAN is available)
- **Compile command:** `javac -d /private/tmp/bob-ui-test-classes src/main/java/*.java`
- **Launch command:** `java -cp /private/tmp/bob-ui-test-classes Bob`
- **Comparison:** Compare stdout exactly after normalizing line endings to LF. Prompts, spaces, and blank lines are significant.
- **Timeout:** 10 seconds per case.
- **Default setup:** Delete `data/bob.txt` before each case so every case starts with an empty task list. Cases that need saved tasks state their own setup instead.
- **Isolation:** Each case starts Bob in a fresh process after its setup is complete.

## UI-001: Add and list todo tasks

**Aim:** Verify that todo tasks can be added, saved without extra console output, and displayed using the `list` command.

**Inputs:**

```text
todo read book
todo return book
list
bye
```

**Expected output:**

```text
 ____        _     
| __ )  ___ | |__  
|  _ \ / _ \| '_ \ 
| |_) | (_) | |_) |
|____/ \___/|_.__/ 

hello im bob !!
how can i help :)
____________________________________________________________
 okays task added:
   [T][ ] read book
 you now have 1 tasks in the list, get to it !!
____________________________________________________________
 okays task added:
   [T][ ] return book
 you now have 2 tasks in the list, get to it !!
____________________________________________________________
 here are your tasks (⌒‿⌒) 加油 !! :
 1.[T][ ] read book
 2.[T][ ] return book
____________________________________________________________
  yippee glad to have helped (＠＾◡＾)
  byebye !! have a good day (๑˃ᴗ˂)ﻭ
____________________________________________________________
```

## UI-002: Mark a task as done

**Aim:** Verify that `mark` changes and saves a task's status from incomplete to complete.

**Inputs:**

```text
todo read book
mark 1
list
bye
```

**Expected output:**

```text
 ____        _     
| __ )  ___ | |__  
|  _ \ / _ \| '_ \ 
| |_) | (_) | |_) |
|____/ \___/|_.__/ 

hello im bob !!
how can i help :)
____________________________________________________________
 okays task added:
   [T][ ] read book
 you now have 1 tasks in the list, get to it !!
____________________________________________________________
 yippee task done, i've marked it as so:
   [T][X] read book
____________________________________________________________
 here are your tasks (⌒‿⌒) 加油 !! :
 1.[T][X] read book
____________________________________________________________
  yippee glad to have helped (＠＾◡＾)
  byebye !! have a good day (๑˃ᴗ˂)ﻭ
____________________________________________________________
```

## UI-003: Unmark a completed task

**Aim:** Verify that `unmark` changes and saves a completed task back to incomplete.

**Inputs:**

```text
todo read book
mark 1
unmark 1
list
bye
```

**Expected output:**

```text
 ____        _     
| __ )  ___ | |__  
|  _ \ / _ \| '_ \ 
| |_) | (_) | |_) |
|____/ \___/|_.__/ 

hello im bob !!
how can i help :)
____________________________________________________________
 okays task added:
   [T][ ] read book
 you now have 1 tasks in the list, get to it !!
____________________________________________________________
 yippee task done, i've marked it as so:
   [T][X] read book
____________________________________________________________
 okie, i've marked this task incomplete:
   [T][ ] read book
____________________________________________________________
 here are your tasks (⌒‿⌒) 加油 !! :
 1.[T][ ] read book
____________________________________________________________
  yippee glad to have helped (＠＾◡＾)
  byebye !! have a good day (๑˃ᴗ˂)ﻭ
____________________________________________________________
```

## UI-004: Add a deadline

**Aim:** Verify that a deadline stores and displays its description and deadline.

**Inputs:**

```text
deadline return book /by Sunday
list
bye
```

**Expected output:**

```text
 ____        _     
| __ )  ___ | |__  
|  _ \ / _ \| '_ \ 
| |_) | (_) | |_) |
|____/ \___/|_.__/ 

hello im bob !!
how can i help :)
____________________________________________________________
 okays task added:
   [D][ ] return book (by: Sunday)
 you now have 1 tasks in the list, get to it !!
____________________________________________________________
 here are your tasks (⌒‿⌒) 加油 !! :
 1.[D][ ] return book (by: Sunday)
____________________________________________________________
  yippee glad to have helped (＠＾◡＾)
  byebye !! have a good day (๑˃ᴗ˂)ﻭ
____________________________________________________________
```

## UI-005: Add an event

**Aim:** Verify that an event stores and displays its description, start time, and end time.

**Inputs:**

```text
event project meeting /from 2pm /to 4pm
list
bye
```

**Expected output:**

```text
 ____        _     
| __ )  ___ | |__  
|  _ \ / _ \| '_ \ 
| |_) | (_) | |_) |
|____/ \___/|_.__/ 

hello im bob !!
how can i help :)
____________________________________________________________
 okays task added:
   [E][ ] project meeting (from: 2pm to: 4pm)
 you now have 1 tasks in the list, get to it !!
____________________________________________________________
 here are your tasks (⌒‿⌒) 加油 !! :
 1.[E][ ] project meeting (from: 2pm to: 4pm)
____________________________________________________________
  yippee glad to have helped (＠＾◡＾)
  byebye !! have a good day (๑˃ᴗ˂)ﻭ
____________________________________________________________
```

## UI-012: Load saved tasks on startup

**Aim:** Verify that Bob loads todo, deadline, and event tasks, including their completion states, from the data file when it starts.

**Setup:** Create `data/bob.txt` with these contents:

```text
T | 1 | read book
D | 0 | return book | Sunday
E | 1 | project meeting | 2pm | 4pm
```

**Inputs:**

```text
list
bye
```

**Expected output:**

```text
 ____        _     
| __ )  ___ | |__  
|  _ \ / _ \| '_ \ 
| |_) | (_) | |_) |
|____/ \___/|_.__/ 

hello im bob !!
how can i help :)
____________________________________________________________
 here are your tasks (⌒‿⌒) 加油 !! :
 1.[T][X] read book
 2.[D][ ] return book (by: Sunday)
 3.[E][X] project meeting (from: 2pm to: 4pm)
____________________________________________________________
  yippee glad to have helped (＠＾◡＾)
  byebye !! have a good day (๑˃ᴗ˂)ﻭ
____________________________________________________________
```

## UI-006: Reject malformed deadline input

**Aim:** Verify that a deadline without `/by` is rejected.

**Inputs:**

```text
deadline return book
bye
```

**Expected output:**

```text
 ____        _     
| __ )  ___ | |__  
|  _ \ / _ \| '_ \ 
| |_) | (_) | |_) |
|____/ \___/|_.__/ 

hello im bob !!
how can i help :)
____________________________________________________________
 oopsies !! (´ ∀ ` *) a deadline needs /by and a date or time, eg play /by today
____________________________________________________________
  yippee glad to have helped (＠＾◡＾)
  byebye !! have a good day (๑˃ᴗ˂)ﻭ
____________________________________________________________
```

## UI-007: Reject malformed event input

**Aim:** Verify that an event without `/from` and `/to` is rejected.

**Inputs:**

```text
event project meeting
bye
```

**Expected output:**

```text
 ____        _     
| __ )  ___ | |__  
|  _ \ / _ \| '_ \ 
| |_) | (_) | |_) |
|____/ \___/|_.__/ 

hello im bob !!
how can i help :)
____________________________________________________________
 oopsies !! (´ ∀ ` *) an event needs /from and /to, eg event meeting /from 2pm /to 4pm
____________________________________________________________
  yippee glad to have helped (＠＾◡＾)
  byebye !! have a good day (๑˃ᴗ˂)ﻭ
____________________________________________________________
```

## UI-008: Handle an unknown command

**Aim:** Verify that Bob handles an unrecognized command.

**Inputs:**

```text
blah
bye
```

**Expected output:**

```text
 ____        _     
| __ )  ___ | |__  
|  _ \ / _ \| '_ \ 
| |_) | (_) | |_) |
|____/ \___/|_.__/ 

hello im bob !!
how can i help :)
____________________________________________________________
 oopsies !! (´ ∀ ` *) pls try either one of list, todo, deadline, event, mark, unmark, delete, or bye
____________________________________________________________
  yippee glad to have helped (＠＾◡＾)
  byebye !! have a good day (๑˃ᴗ˂)ﻭ
____________________________________________________________
```

## UI-011: Delete a task

**Aim:** Verify that `delete` removes and saves the selected task, reports the new count, and renumbers the remaining tasks.

**Inputs:**

```text
todo read book
deadline return book /by June 6th
event project meeting /from Aug 6th 2pm /to 4pm
todo join sports club
todo borrow book
delete 3
list
bye
```

**Expected output:**

```text
 ____        _     
| __ )  ___ | |__  
|  _ \ / _ \| '_ \ 
| |_) | (_) | |_) |
|____/ \___/|_.__/ 

hello im bob !!
how can i help :)
____________________________________________________________
 okays task added:
   [T][ ] read book
 you now have 1 tasks in the list, get to it !!
____________________________________________________________
 okays task added:
   [D][ ] return book (by: June 6th)
 you now have 2 tasks in the list, get to it !!
____________________________________________________________
 okays task added:
   [E][ ] project meeting (from: Aug 6th 2pm to: 4pm)
 you now have 3 tasks in the list, get to it !!
____________________________________________________________
 okays task added:
   [T][ ] join sports club
 you now have 4 tasks in the list, get to it !!
____________________________________________________________
 okays task added:
   [T][ ] borrow book
 you now have 5 tasks in the list, get to it !!
____________________________________________________________
 okays here's the task i deleted: 
   [E][ ] project meeting (from: Aug 6th 2pm to: 4pm)
 pls get to the remaining 4 tasks in your list
____________________________________________________________
 here are your tasks (⌒‿⌒) 加油 !! :
 1.[T][ ] read book
 2.[D][ ] return book (by: June 6th)
 3.[T][ ] join sports club
 4.[T][ ] borrow book
____________________________________________________________
  yippee glad to have helped (＠＾◡＾)
  byebye !! have a good day (๑˃ᴗ˂)ﻭ
____________________________________________________________
```

## UI-009: Reject empty task details

**Aim:** Verify that task commands reject missing descriptions and date or time fields with specific guidance.

**Inputs:**

```text
todo
deadline /by Sunday
deadline return book /by
event /from 2pm /to 4pm
event meeting /from  /to 4pm
event meeting /from 2pm /to
bye
```

**Expected output:**

```text
 ____        _     
| __ )  ___ | |__  
|  _ \ / _ \| '_ \ 
| |_) | (_) | |_) |
|____/ \___/|_.__/ 

hello im bob !!
how can i help :)
____________________________________________________________
 oopsies !! (´ ∀ ` *) oopsies a todo needs a desc, eg: todo sleep
____________________________________________________________
 oopsies !! (´ ∀ ` *) pls give a desc before /by.
____________________________________________________________
 oopsies !! (´ ∀ ` *) pls give a date or time after /by.
____________________________________________________________
 oopsies !! (´ ∀ ` *) pls gimme event desc before /from.
____________________________________________________________
 oopsies !! (´ ∀ ` *) pls gimme start time after /from.
____________________________________________________________
 oopsies !! (´ ∀ ` *) pls gimme end time after /to.
____________________________________________________________
  yippee glad to have helped (＠＾◡＾)
  byebye !! have a good day (๑˃ᴗ˂)ﻭ
____________________________________________________________
```

## UI-010: Reject invalid task numbers

**Aim:** Verify that mark and unmark commands handle missing, non-numeric, and out-of-range task numbers without crashing.

**Inputs:**

```text
mark
mark one
mark 1
todo read book
mark 2
unmark 0
bye
```

**Expected output:**

```text
 ____        _     
| __ )  ___ | |__  
|  _ \ / _ \| '_ \ 
| |_) | (_) | |_) |
|____/ \___/|_.__/ 

hello im bob !!
how can i help :)
____________________________________________________________
 oopsies !! (´ ∀ ` *) can't help if idk which task no
____________________________________________________________
 oopsies !! (´ ∀ ` *) enter a valid task no pls
____________________________________________________________
 oopsies !! (´ ∀ ` *) can't do anyth if there's no task
____________________________________________________________
 okays task added:
   [T][ ] read book
 you now have 1 tasks in the list, get to it !!
____________________________________________________________
 oopsies !! (´ ∀ ` *) task doesn't exist, whats your fav no from 1 to 1?
____________________________________________________________
 oopsies !! (´ ∀ ` *) task doesn't exist, whats your fav no from 1 to 1?
____________________________________________________________
  yippee glad to have helped (＠＾◡＾)
  byebye !! have a good day (๑˃ᴗ˂)ﻭ
____________________________________________________________
```
