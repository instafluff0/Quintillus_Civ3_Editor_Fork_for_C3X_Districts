
package com.civfanatics.civ3.xplatformeditor.specialty;

import com.civfanatics.civ3.biqFile.BIQSection;
import static com.civfanatics.civ3.xplatformeditor.specialty.PredicateFactory.logger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.function.BiPredicate;

/**
 * Shared predicate utilities.  Currently includes tokenization, and evaluation
 * of predicates.  Couldn't get generics working nicely with evaluation, so in
 * the end went Java 1.4 style without generics, which works perfectly well.
 * @author Andrew
 */
public class PredicateCommonFunctions {
    
    /**
     * Tokenizes the filter text into key-value pairs.  This is reusable across
     * multiple tabs' filters, and can be updated to add core functionality to
     * all of them (such as supported quoted search terms).
     * @param filterText
     * @return 
     */
    public static List<String> tokenize(String filterText) {
        
        List<String>completedTokens = new ArrayList<String>();

        String[] quoteTokens = filterText.split("\"| ");
        String tokenBeingConstructed = "";
        boolean appendLoopToken = false;
        for (String loopToken : quoteTokens) {
            if (logger.isTraceEnabled()) {
                logger.trace("Loop Token: " + loopToken);
            }

            if (appendLoopToken) {
                if (loopToken.length() > 0) {
                    //not an empty string; still within the quotes
                    //Add a space since it won't be in token, but that's why the quotes would be there
                    tokenBeingConstructed = tokenBeingConstructed + loopToken + " ";
                }
                else {
                    //is an empty string; reached end-quote-then-space
                    //trim before adding to remove the extra space that the "if" would have added before the end-quote
                    completedTokens.add(tokenBeingConstructed.trim());
                    tokenBeingConstructed = ""; //for next one
                    appendLoopToken = false;
                }
            }
            else if (loopToken.endsWith(">") || loopToken.endsWith("<") || loopToken.endsWith("=")) {
                appendLoopToken = true;
                tokenBeingConstructed = loopToken;
            }
            else {
                completedTokens.add(loopToken);
            }
        }

        if (!tokenBeingConstructed.equals("")) {
            //The last term had quotes and thus we didn't trigger the loopToken.length == 0 condition by having a quote, then a space
            //Thus add the search term
            completedTokens.add(tokenBeingConstructed.trim());
        }
        
        //Make parentheses into their own tokens
        List<String> parenTokens = new LinkedList<>();
        for (String token : completedTokens) {
            while (token.startsWith("(")) {
                parenTokens.add("(");
                token = token.substring(1);
            }
            if (token.endsWith(")")) {
                parenTokens.add(token.substring(0, token.indexOf(")")));
            }
            else {
                parenTokens.add(token);
            }
            while (token.endsWith(")")) {
                parenTokens.add(")");
                token = token.substring(0, token.length() - 1);
            }
        }
        completedTokens = parenTokens;

        if (logger.isTraceEnabled()) {
            logger.trace("Completed Tokens: " + completedTokens.size());
            for (String s : completedTokens) {
                logger.trace(s);
            }
        }
        return completedTokens;
    }
    
    static boolean evaluatePredicates(BIQSection tech, List<BiPredicate> thePredicates, List<String> validatedTokens) {
        
        Iterator<String> tokenIterator = validatedTokens.iterator();
        
        int tokenIndex = 0;
        
        List<BiPredicate> predicatesAfterParens = new LinkedList<>();
        List<String> tokensAfterParens = new LinkedList<>();
        
        int indexOfOpeningParen = -1;
        int predicateIndexOfOpeningParen = -1;
        int parenCount = 0;
        int predicateIndex = 0;
        List<String> tokensThatDontHavePredicates = Arrays.asList("(", ")", "or" );
        while (tokenIterator.hasNext()) {
            String nextToken = tokenIterator.next();
            if (tokensThatDontHavePredicates.contains(nextToken)) {
                predicateIndex--;
            }
            if (nextToken.equals("(")) {
                if (indexOfOpeningParen == -1) {
                    indexOfOpeningParen = tokenIndex;
                    predicateIndexOfOpeningParen = predicateIndex;
                }
                parenCount++;
            }
            else if (nextToken.equals(")")) {
                if (indexOfOpeningParen == -1) {
                    //mismatched parens
                    return false;
                }
                parenCount--;
                if (parenCount == 0) {
                    //Found matching.  Evaluate this and then keep going
                    boolean isParenExpressionTrue = evaluatePredicates(tech, thePredicates.subList(predicateIndexOfOpeningParen + 1, predicateIndex + 1), validatedTokens.subList(indexOfOpeningParen + 1, tokenIndex));
                    //so i guess we want to treat whatever was in parens as its own solitary unit.  it might still be needed as part of an "or" or "and"
                    //but if we modify it here, our iterator will break, and if we break out here, we might miss future parens...
                    predicatesAfterParens.add((t, s) -> {
                        return isParenExpressionTrue ? true : false;
                    });
                    tokensAfterParens.add("alreadyEvaluated");
                    indexOfOpeningParen = -1;   //reset so we evaluate the remainder properly!
                }
            }
            else if (indexOfOpeningParen == -1) {
                //regular token without parens
                if (!nextToken.equals("or")) {
                    predicatesAfterParens.add(thePredicates.get(predicateIndex));
                }
                tokensAfterParens.add(nextToken);
            }
            tokenIndex++;
            predicateIndex++;
        }
        
        thePredicates = null;
        validatedTokens = null;
        
        tokenIterator = tokensAfterParens.iterator();
        tokenIndex = 0;
        while (tokenIterator.hasNext()) {
            if (tokenIterator.next().equals("or")) {
                //Split
                List<String> tokensA = tokensAfterParens.subList(0, tokenIndex);
                List<String> tokensB = tokensAfterParens.subList(tokenIndex + 1, tokensAfterParens.size());
                List<BiPredicate> predicatesA = predicatesAfterParens.subList(0, tokenIndex);
                List<BiPredicate> predicatesB = predicatesAfterParens.subList(tokenIndex, predicatesAfterParens.size());
                boolean resultA = evaluatePredicates(tech, predicatesA, tokensA);
                boolean resultB = evaluatePredicates(tech, predicatesB, tokensB);
                return resultA || resultB;
            }
            tokenIndex++;
        }
        
        tokenIterator = tokensAfterParens.iterator();
        if (!tokenIterator.hasNext()) {
            return true;
        }
        
        for (BiPredicate listPredicate : predicatesAfterParens) {
            String predicateQuery = tokenIterator.next();
            //TODO: Cannot figure out how to genericize this call :(
            if (!listPredicate.test(tech, predicateQuery)) {
                return false;
            }
        }
        return true;
    }
}
