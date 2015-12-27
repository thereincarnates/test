package com.movieztalk.game.model;

public enum IndustryType {

  BOLLYWOOD,
  HOLLYWOOD,
  LOLLYWOOD,
  ;

  @Override
  public String toString(){
    return this.name().toLowerCase();
  }

}
