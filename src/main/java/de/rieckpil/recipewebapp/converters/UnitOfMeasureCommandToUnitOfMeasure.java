package de.rieckpil.recipewebapp.converters;

import de.rieckpil.recipewebapp.commands.UnitOfMeasureCommand;
import de.rieckpil.recipewebapp.domain.UnitOfMeasure;
import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

@Component
public class UnitOfMeasureCommandToUnitOfMeasure implements Converter<UnitOfMeasureCommand, UnitOfMeasure> {

    @Synchronized
    @Nullable
    @Override
    public UnitOfMeasure convert(UnitOfMeasureCommand unitOfMeasureCommand) {
        if(unitOfMeasureCommand == null){
            return null;
        }

        final UnitOfMeasure uom = new UnitOfMeasure();
        uom.setDescription(unitOfMeasureCommand.getDescription());
        uom.setId(unitOfMeasureCommand.getId());
        return uom;
    }
}
