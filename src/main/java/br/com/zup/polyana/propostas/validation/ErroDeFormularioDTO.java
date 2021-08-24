package br.com.zup.polyana.propostas.validation;

import java.util.ArrayList;
import java.util.List;

public class ErroDeFormularioDTO {

	private List<String> globalErrorMessages = new ArrayList<>();
	private List<ErroDeCampoSaidaDto> fieldErrors = new ArrayList<>();

	public void addError(String message) {
		globalErrorMessages.add(message);
	}

	public void addFieldError(String field, String message) {
		ErroDeCampoSaidaDto fieldError = new ErroDeCampoSaidaDto(field, message);
		fieldErrors.add(fieldError);
	}

	public List<String> getGlobalErrorMessages() {
		return globalErrorMessages;
	}

	public List<ErroDeCampoSaidaDto> getErrors() {
		return fieldErrors;
	}

	public int getNumberOfErrors() {
		return this.globalErrorMessages.size() + this.fieldErrors.size();
	}
}
