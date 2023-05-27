package com.cak.mcmg.core;

import com.cak.mcmg.Main;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.function.Consumer;

/**
 * Represents a timed event in a game or application.
 *
 * @author ChatGPT for docs
 */
public class TimedEvent {
  
  /**
   * Initializes a repeating task that calls the {@link #tick()} method on each
   * {@link TimedEvent} object in the {@link #timedEvents} list every 20 ticks.
   */
  public static void initClock() {
    new BukkitRunnable() {
      @Override
      public void run() {
        ArrayList<TimedEvent> timedEventIterable = new ArrayList<>(timedEvents);
        for (TimedEvent event : timedEventIterable) {
          event.tick();
        }
        
        endedTimeEvents.forEach(timedEvent -> timedEvents.remove(timedEvent));
        endedTimeEvents = new ArrayList<>();
      }
    }.runTaskTimer(Main.plugin, 0L, 20L);
  }
  
  /**
   * A list of all {@link TimedEvent} objects.
   */
  static ArrayList<TimedEvent> timedEvents = new ArrayList<>();
  
  /**
   * A list of all {@link TimedEvent} objects that are to be removed at the end of the tick.
   */
  static ArrayList<TimedEvent> endedTimeEvents = new ArrayList<>();
  
  /**
   * The name of the event.
   */
  final String name;
  
  /**
   * The amount of time left for the event to run.
   */
  int timeLeft;
  /**
   * If this should display on the countdown at scoreboard widget.
   */
  boolean display;
  
  /**
   * A {@link Runnable} object that will be called when the event ends.
   */
  Runnable onEnd;
  
  /**
   * A {@link Consumer} object that will be called on each tick of the event, with the
   * current {@link #timeLeft} value as the input.
   */
  Consumer<Integer> onTick;
  
  /**
   * Constructs a new {@link TimedEvent} with the given name and time left.
   *
   * @param name     the name of the event
   * @param timeLeft the amount of time left for the event to run
   */
  public TimedEvent(String name, int timeLeft, boolean display) {
    this.name = name;
    this.timeLeft = timeLeft;
    this.display = display;
    timedEvents.add(this);
  }
  
  /**
   * Sets the {@link #onEnd} field to the given {@link Runnable} object.
   *
   * @param onEnd the {@link Runnable} object to be called when the event ends
   */
  public TimedEvent setOnEnd(Runnable onEnd) {
    this.onEnd = onEnd;
    return this;
  }
  
  /**
   * Sets the {@link #onTick} field to the given {@link Consumer} object.
   *
   * @param onTick the {@link Consumer} object to be called on each tick of the event
   */
  public TimedEvent setOnTick(Consumer<Integer> onTick) {
    this.onTick = onTick;
    return this;
  }
  
  /**
   * Returns if this timer should display
   */
  public boolean doesDisplay() {
    return display;
  }
  
  /**
   * Returns the name of this timer
   */
  public String getName() {
    return name;
  }
  
  /**
   * Returns the ticks left remaining on this timer
   */
  public int getTicksLeft() {
    return timeLeft;
  }
  
  /**
   * Returns the ticks left formatted as mm:ss
   */
  public String getTimeLeftString() {
    String sec = timeLeft % 60 + "";
    String min = (int) Math.floor(timeLeft / 60.0) + "";
    return (min.length() > 1 ? "" : "0") + min + ":" + (sec.length() > 1 ? "" : "0") + sec;
  }
  
  /**
   * Decrements the {@link #timeLeft} field by 1 and calls the {@link #onTick} consumer
   * if it is not {@code null}. If the {@link #timeLeft} field becomes 0, the {@link #onEnd}
   * {@link Runnable} is called.
   */
  public void tick() {
    
    if (timeLeft > 0) {
      timeLeft -= 1;
      if (onTick != null) {
        onTick.accept(timeLeft);
      }
    } else {
      onEnd.run();
      endedTimeEvents.add(this);
    }
    
  }
  
  /**
   * Returns all timed events active
   */
  public static ArrayList<TimedEvent> getTimedEvents() {
    return timedEvents;
  }
}
