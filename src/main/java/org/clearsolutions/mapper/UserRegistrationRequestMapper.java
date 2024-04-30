package org.clearsolutions.mapper;

import org.clearsolutions.dto.UserRegistrationRequest;
import org.clearsolutions.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserRegistrationRequestMapper extends Mappable<User, UserRegistrationRequest> {
}
