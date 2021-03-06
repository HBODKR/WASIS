package br.unicamp.fnjv.wasis.features;

/**
 * Audio Feature Preprocessing.
 * 
 * @author Leandro Tacioli
 * @version 2.0 - 27/Set/2017
 */
public class Preprocessing {
	/** Pre-Emphasis Alpha (Set to 0 if no pre-emphasis should be performed) */
    private final static double PRE_EMPHASIS_ALPHA = 0.95;
    
    /** Number of samples per frame. */
    private final static int FRAME_LENGTH = 1024;
    
    /** Number of overlapping samples (Usually 50% of the <i>FRAME_LENGTH</i>). */
    private final static int OVERLAP_SAMPLES = FRAME_LENGTH / 2;
    
    /**
     * Audio Feature Preprocessing.
     */
    private Preprocessing() {
		
	}

	/**
     * Perform pre-emphasis to equalize amplitude of high and low frequency.
     * 
     * @param audioSignal - Audio Signal
     * 
     * @return preEmphasis
     */
    public static double[] preEmphasis(double[] audioSignal) {
        double[] preEmphasis = new double[audioSignal.length];
        
        for (int indexSignal = 1; indexSignal < audioSignal.length; indexSignal++) {
        	preEmphasis[indexSignal] = audioSignal[indexSignal] - PRE_EMPHASIS_ALPHA * audioSignal[indexSignal - 1];
        }
        
        return preEmphasis;
    }
    
    /**
     * Performs Frame Blocking to break down an audio signal into frames.<br>
     * <br>
     * Default <i>FRAME_LENGTH</i> = 1024.<br>
     * Default <i>OVERLAP_SAMPLES</i> = FRAME_LENGTH / 2.
     * 
     * @param audioSignal - Audio Signal
     */
    public static double[][] framing(double[] audioSignal) {
    	return framing(audioSignal, FRAME_LENGTH, OVERLAP_SAMPLES);
    }
    
    /**
     * Performs Frame Blocking to break down an audio signal into frames.
     * 
     * @param audioSignal       - Audio Signal
     * @param intFrameLength    - Frame Length
     * @param intOverlapSamples - Overlap Samples
     */
    public static double[][] framing(double[] audioSignal, int intFrameLength, int intOverlapSamples) {
        double dblNumFrames = (double) audioSignal.length / (double) (intFrameLength - intOverlapSamples);
        
        // unconditionally round up
        if ((dblNumFrames / (int) dblNumFrames) != 1) {
        	dblNumFrames = (int) dblNumFrames + 1;
        }
        
        // use zero padding to fill up frames with not enough samples
        double[] paddedSignal = new double[(int) dblNumFrames * intFrameLength];
        for (int indexSignal = 0; indexSignal < audioSignal.length; indexSignal++) {
            paddedSignal[indexSignal] = audioSignal[indexSignal];
        }
        
        double[][] frames = new double[(int) dblNumFrames][intFrameLength];
        
        // break down speech signal into frames with specified shift interval to create overlap
        for (int indexFrame = 0; indexFrame < dblNumFrames; indexFrame++){
            for (int indexLength = 0; indexLength < intFrameLength; indexLength++){
                frames[indexFrame][indexLength] = paddedSignal[indexFrame * (intFrameLength - intOverlapSamples) + indexLength];
            }
        }
        
        return frames;
    }
}