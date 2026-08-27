# UI Test Plan

This is the source of truth for Bob's console UI tests. Cases run in order, each in a fresh process.

## Test configuration

- **Java version:** 25 (`sdk use java 25.0.3.fx-zulu` on macOS when SDKMAN is available)
- **Compile command:** `javac -d /private/tmp/bob-ui-test-classes $(find src/main/java -name '*.java' -print)`
- **Launch command:** `java -cp /private/tmp/bob-ui-test-classes bob.Bob`
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

**Aim:** Verify that a deadline parses an ISO date and displays it in a friendly format.

**Inputs:**

```text
deadline return book /by 2019-12-02
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
   [D][ ] return book (by: Dec 02 2019)
 you now have 1 tasks in the list, get to it !!
____________________________________________________________
 here are your tasks (⌒‿⌒) 加油 !! :
 1.[D][ ] return book (by: Dec 02 2019)
____________________________________________________________
  yippee glad to have helped (＠＾◡＾)
  byebye !! have a good day (๑˃ᴗ˂)ﻭ
____________________________________________________________
```

## UI-005: Add an event

**Aim:** Verify that an event parses typed date-times and displays them in a friendly format.

**Inputs:**

```text
event project meeting /from 2019-12-02 1400 /to 2019-12-02 1600
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
   [E][ ] project meeting (from: Dec 02 2019, 2:00 PM to: Dec 02 2019, 4:00 PM)
 you now have 1 tasks in the list, get to it !!
____________________________________________________________
 here are your tasks (⌒‿⌒) 加油 !! :
 1.[E][ ] project meeting (from: Dec 02 2019, 2:00 PM to: Dec 02 2019, 4:00 PM)
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

D | 0 | return book | 2019-12-02
E | 1 | project meeting | 2019-12-02 1400 | 2019-12-02 1600
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
 2.[D][ ] return book (by: Dec 02 2019)
 3.[E][X] project meeting (from: Dec 02 2019, 2:00 PM to: Dec 02 2019, 4:00 PM)
____________________________________________________________
  yippee glad to have helped (＠＾◡＾)
  byebye !! have a good day (๑˃ᴗ˂)ﻭ
____________________________________________________________
```

## UI-013: Handle a malformed data file

**Aim:** Verify that Bob reports corrupt saved data without crashing or exposing a partially loaded task list.

**Setup:** Create `data/bob.txt` with these contents:

```text
T | 0 | valid task
X | 0 | unknown task
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
 oopsies !! (´ ∀ ` *) couldn't load saved tasks: invalid data on line 2
____________________________________________________________
 here are your tasks (⌒‿⌒) 加油 !! :
____________________________________________________________
  yippee glad to have helped (＠＾◡＾)
  byebye !! have a good day (๑˃ᴗ˂)ﻭ
____________________________________________________________
```

## UI-014: Handle a save failure

**Aim:** Verify that Bob reports a disk-write failure, keeps running, and rolls back the unsaved task.

**Setup:** Create a regular file named `data`, preventing creation of `data/bob.txt`.

**Inputs:**

```text
todo read book
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
 oopsies !! (´ ∀ ` *) couldn't save your tasks; nothing was changed
____________________________________________________________
 here are your tasks (⌒‿⌒) 加油 !! :
____________________________________________________________
  yippee glad to have helped (＠＾◡＾)
  byebye !! have a good day (๑˃ᴗ˂)ﻭ
____________________________________________________________
```

## UI-015: Load escaped separator characters

**Aim:** Verify that pipes and backslashes inside saved task fields are decoded as task text rather than separators.

**Setup:** Create `data/bob.txt` with these contents:

```text
T | 0 | read \| review \\ notes
D | 1 | return \| renew | 2019-12-02
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
 1.[T][ ] read | review \ notes
 2.[D][X] return | renew (by: Dec 02 2019)
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
 oopsies !! (´ ∀ ` *) a deadline needs /by and a date, eg play /by 2019-12-02
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
 oopsies !! (´ ∀ ` *) an event needs /from and /to, eg event meeting /from 2019-12-02 1400 /to 2019-12-02 1600
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
deadline return book /by 2019-06-06
event project meeting /from 2019-08-06 1400 /to 2019-08-06 1600
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
   [D][ ] return book (by: Jun 06 2019)
 you now have 2 tasks in the list, get to it !!
____________________________________________________________
 okays task added:
   [E][ ] project meeting (from: Aug 06 2019, 2:00 PM to: Aug 06 2019, 4:00 PM)
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
   [E][ ] project meeting (from: Aug 06 2019, 2:00 PM to: Aug 06 2019, 4:00 PM)
 pls get to the remaining 4 tasks in your list
____________________________________________________________
 here are your tasks (⌒‿⌒) 加油 !! :
 1.[T][ ] read book
 2.[D][ ] return book (by: Jun 06 2019)
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
deadline /by 2019-12-02
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
 oopsies !! (´ ∀ ` *) pls give a date after /by.
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

## UI-016: Reject invalid dates and times

**Aim:** Verify that typed date fields reject invalid formats and that an event cannot end before it starts.

**Inputs:**

```text
deadline return book /by 2/12/2019
event meeting /from 2019-12-02 2pm /to 2019-12-02 1600
event meeting /from 2019-12-02 1800 /to 2019-12-02 1600
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
 oopsies !! (´ ∀ ` *) use yyyy-MM-dd for deadline dates, eg 2019-12-02
____________________________________________________________
 oopsies !! (´ ∀ ` *) use yyyy-MM-dd HHmm for event dates and times, eg 2019-12-02 1800
____________________________________________________________
 oopsies !! (´ ∀ ` *) an event's end cannot be before its start
____________________________________________________________
  yippee glad to have helped (＠＾◡＾)
  byebye !! have a good day (๑˃ᴗ˂)ﻭ
____________________________________________________________
```
