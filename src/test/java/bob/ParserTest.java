package bob;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import bob.command.AddCommand;
import bob.command.ExitCommand;
import bob.command.MarkCommand;

class ParserTest {
    @Test
    void parse_validExitCommand_returnsExitCommand() throws BobException {
        assertInstanceOf(ExitCommand.class, Parser.parse("bye"));
    }

    @Test
    void parse_validMarkCommand_returnsMarkCommand() throws BobException {
        assertInstanceOf(MarkCommand.class, Parser.parse("mark 2"));
    }

    @Test
    void parse_validEvent_returnsAddCommand() throws BobException {
        assertInstanceOf(AddCommand.class,
                Parser.parse("event meeting /from 2026-08-28 1400 /to 2026-08-28 1600"));
    }

    @Test
    void parse_eventEndingBeforeStart_throwsBobException() {
        assertThrows(BobException.class,
                () -> Parser.parse("event meeting /from 2026-08-28 1600 /to 2026-08-28 1400"));
    }

    @Test
    void parse_deadlineWithInvalidDate_throwsBobException() {
        assertThrows(BobException.class, () -> Parser.parse("deadline submit report /by 2026-02-30"));
    }

    @Test
    void parse_unknownCommand_throwsBobException() {
        assertThrows(BobException.class, () -> Parser.parse("remind submit report"));
    }
}
