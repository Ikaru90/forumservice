package com.forumservice.domain;

public final class Views {
    public interface ShortMessage {}
    public interface FullMessage extends ShortMessage {}
    public interface UserWithoutPassword {}
    public interface FullUser extends UserWithoutPassword {}
}
