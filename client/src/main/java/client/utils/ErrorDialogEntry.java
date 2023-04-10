package client.utils;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorDialogEntry {
    private String popupTitle;
    private String message;
}
