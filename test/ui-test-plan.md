# UI Test Plan

This is the source of truth for Bob's console UI tests. Cases run in order, each in a fresh process.

## Test configuration

- **Java version:** 25 (`sdk use java 25.0.3.fx-zulu` on macOS when SDKMAN is available)
- **Compile command:** `javac -d /private/tmp/bob-ui-test-classes src/main/java/*.java`
- **Launch command:** `java -cp /private/tmp/bob-ui-test-classes Bob`
- **Comparison:** Compare stdout exactly after normalizing line endings to LF. Prompts, spaces, and blank lines are significant.
- **Timeout:** 10 seconds per case.
- **Isolation:** Each case starts Bob in a fresh process with an empty in-memory task list.

## UI-001: Add and list todo tasks

**Aim:** Verify that todo tasks can be added and displayed using the `list` command.

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

**Aim:** Verify that `mark` changes a task's status from incomplete to complete.

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

**Aim:** Verify that `unmark` changes a completed task back to incomplete.

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
 please include /by followed by the deadline
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
 please include /from and /to for the event
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
 sorry what??
____________________________________________________________
  yippee glad to have helped (＠＾◡＾)
  byebye !! have a good day (๑˃ᴗ˂)ﻭ
____________________________________________________________
```
