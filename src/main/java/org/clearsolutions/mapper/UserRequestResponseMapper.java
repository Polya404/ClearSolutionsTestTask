package org.clearsolutions.mapper;

import org.clearsolutions.dto.UserRequestResponse;
import org.clearsolutions.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserRequestResponseMapper extends Mappable<User, UserRequestResponse>{
}
