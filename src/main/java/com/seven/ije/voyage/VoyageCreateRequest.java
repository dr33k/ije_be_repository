package com.seven.ije.voyage;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.seven.ije.location.LocationId;
import com.seven.ije.AppRequest;
import jakarta.validation.constraints.Future;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import java.time.ZonedDateTime;

@Validated
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class VoyageCreateRequest implements AppRequest {
    private LocationId departureLocationId;
    private LocationId arrivalLocationId;
    @Future(message = "Past and current dates not allowed")
    private ZonedDateTime departureDateTime;
}