package co.edu.javeriana.easymarket.usersservice.aop;

import co.edu.javeriana.easymarket.usersservice.dtos.UserDTO;
import co.edu.javeriana.easymarket.usersservice.model.User;
import co.edu.javeriana.easymarket.usersservice.services.EmailService;
import co.edu.javeriana.easymarket.usersservice.services.UserService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Aspect
@Component
@ConditionalOnProperty(prefix = "features", name = "notifications", havingValue = "true", matchIfMissing = false)
public class UserNotificationAspect {

    private final EmailService emailService;
    private final UserService userService;

    @Autowired
    public UserNotificationAspect(EmailService emailService, UserService userService) {
        this.emailService = emailService;
        this.userService = userService;
    }

    @AfterReturning("execution(* co.edu.javeriana.easymarket.usersservice.controllers.UsersController.createUser(..))")
    public void notifyCreateUser(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        if (args.length > 0 && args[0] instanceof UserDTO userDTO) {
            if ("customer".equalsIgnoreCase(userDTO.getRol())) return;

            String subject = "Nuevo usuario creado";
            String message = String.format("""
                Se ha creado un nuevo usuario empleado:<br>
                <strong>Nombre:</strong> %s<br>
                <strong>Email:</strong> %s<br>
                <strong>Rol:</strong> %s
                """, userDTO.getFullname(), userDTO.getEmail(), userDTO.getRol());

            sendToAllAdmins(subject, message);
        }
    }

    @AfterReturning("execution(* co.edu.javeriana.easymarket.usersservice.controllers.UsersController.deleteUser(..))")
    public void notifyDeleteUser(JoinPoint joinPoint) {
        String id = (String) joinPoint.getArgs()[0];
        String subject = "Usuario marcado como eliminado";
        String message = "Se eliminó (soft-delete) el usuario con ID:<br>" + formatIdAsQuote(id);

        sendToAllAdmins(subject, message);
    }

    @AfterReturning("execution(* co.edu.javeriana.easymarket.usersservice.controllers.UsersController.restoreUser(..))")
    public void notifyRestoreUser(JoinPoint joinPoint) {
        String id = (String) joinPoint.getArgs()[0];
        String subject = "Usuario restaurado";
        String message = "El usuario con el siguiente ID ha sido restaurado:<br>" + formatIdAsQuote(id);

        sendToAllAdmins(subject, message);
    }

    @AfterReturning("execution(* co.edu.javeriana.easymarket.usersservice.controllers.UsersController.deleteUserPermanently(..))")
    public void notifyDeleteUserPermanently(JoinPoint joinPoint) {
        String id = (String) joinPoint.getArgs()[0];
        String subject = "Usuario eliminado permanentemente";
        String message = "Se eliminó de forma permanente el usuario con ID:<br>" + formatIdAsQuote(id);

        sendToAllAdmins(subject, message);
    }

    @Around("execution(* co.edu.javeriana.easymarket.usersservice.controllers.UsersController.updateUser(..))")
    public Object notifyUpdateUser(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        String id = (String) args[0];
        UserDTO updatedDTO = (UserDTO) args[1];

        User existingUser = userService.getUser(id);
        String oldRol = existingUser.getRol();
        String newRol = updatedDTO.getRol();

        Object result = joinPoint.proceed();

        if ("customer".equalsIgnoreCase(oldRol) && !"customer".equalsIgnoreCase(newRol)) {
            String subject = "Cambio de rol de usuario";
            String message = String.format("""
                El usuario con el siguiente ID ha cambiado su rol de 'customer' a '%s':<br>%s
                """, newRol, formatIdAsQuote(id));
            sendToAllAdmins(subject, message);
        }

        return result;
    }

    private void sendToAllAdmins(String subject, String message) {
        String[] recipients = userService.getAllAdminEmails();
        for (String recipient : recipients) {
            emailService.sendNotification(subject, recipient, message);
        }
    }

    private String formatIdAsQuote(String id) {
        return """
            <blockquote style="background-color: #f0f4ff; border-left: 4px solid #3399ff; padding: 10px 15px; margin: 10px 0; font-family: monospace; color: #003366;">
                %s
            </blockquote>
            """.formatted(id);
    }
}
