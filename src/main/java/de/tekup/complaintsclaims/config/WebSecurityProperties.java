package de.tekup.complaintsclaims.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebSecurityProperties {

    @Getter
    @Value("${allowed.routes.public.post}")
    private String[] allowedPostRoutes;

    @Getter
    @Value("${allowed.routes.public.get}")
    private String[] allowedGetRoutes;

    @Getter
    @Value("${allowed.routes.admin.get}")
    private String[] allowedAdminGetRoutes;
}