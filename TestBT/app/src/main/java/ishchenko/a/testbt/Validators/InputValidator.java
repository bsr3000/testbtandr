package ishchenko.a.testbt.Validators;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.util.regex.Pattern;

/**
 * Created by andrey on 08.03.17.
 */
public abstract class InputValidator implements TextWatcher {
    private final EditText editText;
    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    public InputValidator(EditText editText) {
        this.editText = editText;
    }

    public abstract boolean validate(EditText editText, String text);

    @Override
    final public void afterTextChanged(Editable s) {
        String text = editText.getText().toString();
        validate(editText, text);
    }

    @Override
    final public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

    @Override
    final public void onTextChanged(CharSequence s, int start, int before, int count) {}
}
