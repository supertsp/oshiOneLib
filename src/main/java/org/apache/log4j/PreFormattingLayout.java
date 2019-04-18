package org.apache.log4j;


import org.apache.log4j.spi.LoggingEvent;


/**
 * Interface which when implemented by a Layout implies that the formatting can
 * be precomputed and retained.
 * 
 * @author Jess Holle
 */
public interface  PreFormattingLayout
{
  /** To be called prior to entering any synchronization blocks but after one is
   *  sure 'loggingEvent' is to be logged.
   */
  public void  preFormat( LoggingEvent loggingEvent );
  
  /** Should always be called after leaving all synchronization if and only if
   *  preFormat() has been called.
   */
  public void  clearPreFormat( LoggingEvent loggingEvent );
}
