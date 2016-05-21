/*F********************************************************************************
 * openXBOW - the Passau Open-Source Crossmodal Bag-of-Words Toolkit
 * 
 * (c) 2016, Maximilian Schmitt, Bj�rn Schuller: University of Passau. 
 *     All rights reserved.
 * 
 * Any form of commercial use and redistribution is prohibited, unless another
 * agreement between you and the copyright holder exists.
 * 
 * Contact: maximilian.schmitt@uni-passau.de
 * 
 * If you use openXBOW or any code from openXBOW in your research work,
 * you are kindly asked to acknowledge the use of openXBOW in your publications.
 * See the file CITING.txt for details.
 *******************************************************************************E*/

package openxbow.nlp;

public class String2WordVector {
    public String2WordVector() {
        
    }
    
    public String[] string2WordVector(String str, String stopCharacters, int nGram, int nCharGram) {
        final boolean bToUpperCase = true; /* ASR output is UPPERCASE */
        
        /* Tokenizer - stop characters */
        for (int k=0; k < stopCharacters.length(); k++) {
            str = str.replace(stopCharacters.substring(k,k+1), "");                    
        }
        
        String[] wordVector = str.split(" ");
        for (int k=0; k < wordVector.length; k++) {
            wordVector[k] = wordVector[k].replace(" ", "");
            if (bToUpperCase) {
                wordVector[k] = wordVector[k].toUpperCase();
            }
        }
        
        /* N-Grams / N-Character-Grams */
        if (nGram > 1 && nCharGram > 0) {
            System.err.println("Error in String2WordVector: only either nGram or nCharGram may be used!");
            return wordVector;
        }
        
        if (nGram > 1 && wordVector.length > 1) {
            wordVector = getNGrams(wordVector, nGram);
        }
        else if (nCharGram > 0 && wordVector.length > 0) {
            wordVector = getNCharacterGrams(wordVector, nCharGram);
        }
        
        int numNonEmpty = 0;
        for (int k=0; k < wordVector.length; k++) {
            if (!wordVector[k].isEmpty()) {
                numNonEmpty++;
            }
        }        
        String[] finWordVector = new String[numNonEmpty];
        int c = 0;
        for (int k=0; k < wordVector.length; k++) {
            if (!wordVector[k].isEmpty()) {
                finWordVector[c++] = wordVector[k];
            }
        }
        
        return finWordVector;
    }
    
    
    private String[] getNGrams(String[] wordVector, int nGram) {
        /* nGram is the maximum number of successive words, so all subsets are also kept */
        int sizeNewVector = wordVector.length;
        for (int n=2; n <= nGram; n++) {
            sizeNewVector += wordVector.length - n + 1;
        }
        
        String[] wordVectorNGram = new String[sizeNewVector];
        for (int k=0; k < wordVector.length; k++) {
            wordVectorNGram[k] = wordVector[k];
        }
        int c = wordVector.length;
        for (int n=2; n <= nGram; n++) {
            for (int k=0; k < wordVector.length-n+1; k++) {
                String strNGram = wordVector[k];
                for (int m=1; m <= n-1; m++) {
                    strNGram = strNGram.concat(" ");
                    strNGram = strNGram.concat(wordVector[k+m]);
                }
                wordVectorNGram[c++] = strNGram;
            }
        }
        
        return wordVectorNGram;
    }
    
    
    private String[] getNCharacterGrams(String[] wordVector, int nCharGram) {
        /* nCharGram is the number of successive characters */
        
        String allCharacters = "";
        for (int k=0; k < wordVector.length; k++) {
            allCharacters = allCharacters.concat(wordVector[k]);
        }
        int numCharacters = allCharacters.length();
        
        if (numCharacters < nCharGram) {
            String[] wordVectorNCharGram = new String[0];
            return wordVectorNCharGram;
        }
        
        String[] wordVectorNCharGram = new String[numCharacters-nCharGram+1];
        
        for (int k=0; k < wordVectorNCharGram.length; k++) {
            wordVectorNCharGram[k] = allCharacters.substring(k, k+nCharGram);
        }
        
        return wordVectorNCharGram;
    }
}
