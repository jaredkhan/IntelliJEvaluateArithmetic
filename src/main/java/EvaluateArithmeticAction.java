import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.*;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import com.fathzer.soft.javaluator.DoubleEvaluator;


public class EvaluateArithmeticAction extends AnAction {
    private static DoubleEvaluator evaluator = new DoubleEvaluator();

    /**
     * Evaluates the selection at each caret as a mathematical expression
     * and replaces the content of the selection with the answer.
     * @param event  Event related to this action
     */
    @Override
    public void actionPerformed(@NotNull final AnActionEvent event) {
        final Editor editor = event.getRequiredData(CommonDataKeys.EDITOR);
        final Project project = event.getRequiredData(CommonDataKeys.PROJECT);
        final Document document = editor.getDocument();

        WriteCommandAction.runWriteCommandAction(project, () -> {
            for (Caret caret : editor.getCaretModel().getAllCarets()) {
                String new_text = caret.getSelectedText();
                if (new_text != null) {
                    document.replaceString(
                            caret.getSelectionStart(),
                            caret.getSelectionEnd(),
                            evaluate(new_text)
                    );
                }
                caret.removeSelection();
            }
        });
    }

    /**
     * Evaluate a given string as an arithmetic expression and return the result as a string
     * @param expression_string The string to evaluate as an arithmetic expression
     * @return The result of the evaluation if possible or the unchanged string if not
     */
    static String evaluate(@NotNull String expression_string) {
        // Only allow arithmetic characters and whitespace
        if (expression_string.matches("^[0-9+\\-/*^().\\s]*(=\\s*)?")) {
            try {
                boolean append_result_to_expression = expression_string.contains("=");
                // Normalise all whitespace to spaces, remove the optional equal sign (=).
                // This means groovy expects a single expression
                String new_string = expression_string.replaceAll("\\s|=", " ");

                String answer = String.valueOf(evaluator.evaluate(new_string));
                if (answer.contains(".")) {
                    // Strip all trailing zeroes after decimal point
                    answer = answer.replaceAll("0*$","");

                    // Remove the decimal point if there's nothing left after it
                    answer = answer.replaceAll("\\.$","");

                }
                return append_result_to_expression ? (expression_string + answer) : answer;
            } catch (IllegalArgumentException e) {
                // The expression was not a valid arithmetic expression
                return expression_string;
            }
        }
        return expression_string;
    }

    @Override
    public void update(@NotNull final AnActionEvent event) {
        final Project project = event.getProject();
        final Editor editor = event.getData(CommonDataKeys.EDITOR);
        event.getPresentation().setEnabledAndVisible(
            project != null &&
            editor != null &&
            editor.getSelectionModel().hasSelection()
        );
    }
}