package main.api.response;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import main.api.response.model.Tag;

import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
public class TagResponse {
    List<Tag> tags;
}