package com.gmail.perhapsitisyeazz.command;

import com.gmail.perhapsitisyeazz.IHaveNoFriend;
import com.gmail.perhapsitisyeazz.manager.SubCommandFunc.*;
import com.gmail.perhapsitisyeazz.util.Message;
import com.moderocky.mask.command.ArgOfflinePlayer;
import com.moderocky.mask.command.ArgPlayer;
import com.moderocky.mask.command.Commander;
import com.moderocky.mask.template.WrappedCommand;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class MainCommand extends Commander<CommandSender> implements WrappedCommand {

    private final IHaveNoFriend instance = IHaveNoFriend.getInstance();

    @Override
    public CommandImpl create() {
        return command("friend")
                .arg("help", sender -> HelpFriend.friendHelpCommand(sender, this))
                .arg("toggle", sender -> ToggleFriend.friendToggleCommand((Player) sender))
                .arg("list", sender -> ListFriend.friendListFriend((Player) sender))
                .arg("add", sender -> sender.sendMessage(ChatColor.RED + "You must enter a player."),
                        arg(
                                desc("Send a friend request to a player."),
                                (sender, input) -> AddFriend.friendAddCommand((Player) sender, (Player) input[0]),
                                new ArgPlayer()
                        ))
                .arg("remove", sender -> sender.sendMessage(ChatColor.RED + "You must enter a player."),
                        arg(
                                desc("Remove a friend from your friend list."),
                                (sender, input) -> RemoveFriend.friendRemoveCommand((Player) sender, (OfflinePlayer) input[0]),
                                new ArgOfflinePlayer()
                        ))
                .arg("accept", sender -> sender.sendMessage(ChatColor.RED + "You must enter a player."),
                        arg(
                                desc("Accept a friend request."),
                                (sender, input) -> AcceptFriend.friendAcceptCommand((Player) sender, (OfflinePlayer) input[0]),
                                new ArgOfflinePlayer()
                        ))
                .arg("deny", sender -> sender.sendMessage(ChatColor.RED + "You must enter a player."),
                        arg(
                                desc("Decline a friend request."),
                                (sender, input) -> DenyFriend.friendDenyCommand((Player) sender, (OfflinePlayer) input[0]),
                                new ArgOfflinePlayer()
                        ));
    }

    @Override
    public CommandSingleAction<CommandSender> getDefault() {
        return Message::sendHelpMessage;
    }

    @Override
    public @Nullable List<String> getCompletions(int i, @NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        List<String> strings = getTabCompletions(String.join(" ", args));
        if (strings == null || strings.isEmpty()) return null;
        final List<String> completions = new ArrayList<>();
        StringUtil.copyPartialMatches(args[args.length - 1], strings, completions);
        Collections.sort(completions);
        return completions;
    }

    @Override
    public @NotNull List<String> getAliases() {
        return new ArrayList<>();
    }

    @Override
    public @NotNull String getUsage() {
        return "/" + getCommand();
    }

    @Override
    public @NotNull String getDescription() {
        return "Main command";
    }

    @Override
    public @Nullable String getPermission() {
        return null;
    }

    @Override
    public @Nullable String getPermissionMessage() {
        return null;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if ((args.length > 0 && !(String.join(" ", args).contains("help"))) && !(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Player-only command.");
            return true;
        }
        else return execute(sender, args);
    }
}