package br.com.zup.polyana.propostas.validation;

public class ErroDeCampoSaidaDto {
        private String field;
        private String message;

        ErroDeCampoSaidaDto() { }

        public ErroDeCampoSaidaDto(String field, String message) {
            this.field = field;
            this.message = message;
        }

        public String getField() {
            return field;
        }

        public String getMessage() {
            return message;
        }

}
