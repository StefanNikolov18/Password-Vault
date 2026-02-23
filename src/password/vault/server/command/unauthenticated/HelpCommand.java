package password.vault.server.command.unauthenticated;

import password.vault.server.command.Command;
import password.vault.server.command.CommandResult;

public class HelpCommand implements Command {

    @Override
    public CommandResult execute(String[] args, String currentUser) {
        String help = """
                register <user> <password> <password-repeat> - registration in the system
                login <user> <password> - login with username and password in the system
                logout - to logout from the session
                add-password <website> <user> <password> - to add custom password for website
                generate-password <website> <user> - generating random password for website with given user
                retrieve-credentials <website> <user> - to retrieve password from the website you saved
                remove-password <website> <user> - to remove password for website
                disconnect - disconnect connection with the server""";

        return new CommandResult(null, help);
    }
}
