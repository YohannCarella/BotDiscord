package discord.bot.command.server.managing;

import discord.bot.command.ICommand;
import discord.bot.utils.misc.MessageSenderFactory;
import discord.bot.utils.misc.SharedStringEnum;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.util.List;

public class KickCommand extends ICommand {
    private final String HELP = "Kick one / many users from the server if you're allowed to. \nUsage : `!"+ this.commandName +" @UserA @UserB @UserC ... Reason `";
    private final String KICK_MESSAGE = "You've been kicked because of : ";
    private final String REQUIRE_MENTIONED_USERS = "You must mention the users you want to kick";
    private final String SUCCESS_MESSAGE = "User has been kicked from the server.";
    private final String ACTION_PERFORMED = "Exclure : ";
    private static Logger logger = Logger.getLogger(KickCommand.class);

    public KickCommand(String commandName) {
        super(commandName);
    }

    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        if(args.length != 0 && args[0].equals("help") || args.length < 2) return false;
        else return true;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {
        if(event.getMember().getPermissions().contains(Permission.KICK_MEMBERS)) {
            if(!event.getGuild().getSelfMember().getPermissions().contains(Permission.KICK_MEMBERS)){
                return;
            }
            if(!event.getMessage().getMentionedMembers().isEmpty()) {
                List<Member> targetedUsers = event.getMessage().getMentionedMembers();
                for (Member curr : targetedUsers) {
                    event.getGuild().kick(curr, KICK_MESSAGE + args[args.length - 1]).queue();
                    logger.log(Level.INFO, ACTION_PERFORMED + curr.getUser().getName() + " par " + event.getAuthor().getName() + " sur le serveur : " + event.getGuild().getName());
                    MessageSenderFactory.getInstance().sendSafePrivateMessage(event.getAuthor(), SUCCESS_MESSAGE, event.getTextChannel(), SUCCESS_MESSAGE);
                }
            }else {
                MessageSenderFactory.getInstance().sendSafeMessage(event.getTextChannel(), REQUIRE_MENTIONED_USERS);
            }
        }else {
            event.getMessage().delete().queue();
            MessageSenderFactory.getInstance().sendSafePrivateMessage(event.getAuthor(), SharedStringEnum.NOT_ALLOWED.getSharedString(), event.getTextChannel(), SharedStringEnum.NO_PRIVATE_CHANNEL_OPEN.getSharedString());
        }
    }

    @Override
    public String help() {
        return HELP;
    }

}
