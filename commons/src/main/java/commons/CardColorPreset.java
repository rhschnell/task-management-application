package commons;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CardColorPreset implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @JsonIgnore
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.DETACH},
            mappedBy = "presets", fetch = FetchType.EAGER)
    private List<Card> cards;

    private String name;
    private String backgroundColor;
    private String fontColor;
    private boolean isDefault;

    /**
     * Constructor for the CardColorPreset
     * @param name The name of the preset
     * @param backgroundColor The background-color of the preset
     * @param fontColor The font-color of the preset
     */
    public CardColorPreset(String name, String backgroundColor, String fontColor) {
        this.name = name;
        this.backgroundColor = backgroundColor;
        this.fontColor = fontColor;
        this.isDefault = false;
    }
}
