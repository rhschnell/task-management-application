package commons;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CardColorPreset {

    private String name;
    private String backgroundColor;
    private String fontColor;
    private boolean isDefault;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    public CardColorPreset(String name, String backgroundColor, String fontColor) {
        this.name = name;
        this.backgroundColor = backgroundColor;
        this.fontColor = fontColor;
        this.isDefault = false;
    }
}
