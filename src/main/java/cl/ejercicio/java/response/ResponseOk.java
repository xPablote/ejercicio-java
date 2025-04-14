package cl.ejercicio.java.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseOk<T> {

    private String message;
    private T data;

    public ResponseOk( String message) {
        this.message = message;
    }
}