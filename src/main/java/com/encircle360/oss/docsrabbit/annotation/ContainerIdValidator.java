package com.encircle360.oss.docsrabbit.annotation;

import com.encircle360.oss.docsrabbit.dto.render.AbstractRenderRequestDTO;
import com.encircle360.oss.docsrabbit.dto.render.RenderFormatDTO;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ContainerIdValidator implements ConstraintValidator<ValidateContainerId, AbstractRenderRequestDTO> {

    public boolean isValid(AbstractRenderRequestDTO renderRequestDTO, ConstraintValidatorContext context) {
        if (!renderRequestDTO.getFormat().equals(RenderFormatDTO.XLS)) {
            return true;
        }

        String containerId = renderRequestDTO.getContainerId();
        if (containerId != null && !containerId.isBlank()) {
            return true;
        }

        return false;
    }
}
