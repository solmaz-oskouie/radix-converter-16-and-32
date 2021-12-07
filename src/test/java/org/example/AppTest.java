package org.example;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.github.javafaker.Faker;
import org.apache.commons.lang3.math.NumberUtils;
import org.junit.Before;
import org.junit.Test;

import javax.print.attribute.standard.NumberUp;
import java.math.BigInteger;
import java.util.stream.IntStream;

/**
 * Unit test for converting numbers between 10 <-> 32/16 radixes.
 * In this class we use Long and BigInteger 's methods for converting between radixes,
 * but it seems like the  Long's methods are  developer-friendly and straightforward
 *
 * I guess there are some good third-party libraries which can be useful like libs  that have been list in  following link
 * https://www.google.com/search?q=best+math+library+for+java&client=ubuntu&hs=EoI&channel=fs&ei=s1SvYbCtNYH-kwXLrZ6ADg&oq=best+math+library+in+java&gs_lcp=Cgdnd3Mtd2l6EAEYATIGCAAQFhAeMgYIABAWEB46BwgAEEcQsAM6BQgAEJECOgsIABCABBCxAxCDAToRCC4QgAQQsQMQgwEQxwEQ0QM6EQguEIAEELEDEIMBEMcBEKMCOg4ILhCABBCxAxDHARDRAzoOCC4QgAQQsQMQxwEQowI6BAgAEEM6CAgAEIAEELEDOgsIABCABBCxAxDJAzoFCAAQkgM6BQgAEIAEOgsIABDsAxDEAhCLAzoJCAAQFhAeEIsDOggIABAWEAoQHjoICCEQFhAdEB5KBAhBGABQqAlYv1hgqmpoAXACeACAAc4DiAHAN5IBCjAuNC4xNC40LjOYAQCgAQHIAQi4AQLAAQE&sclient=gws-wiz
 */
public class AppTest {

    private static int radix;
    private static String maxValueStr;


   @Before
    public  void init(){
        radix=32;
        maxValueStr =radix==16?"fffffffffff":"vvvvvvvvvvv";
    }
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue() {
        assertTrue(true);
    }


    /* on radix 32  BigInteger has used  0-9a-v chars for displaying digits */
    @Test
    public void printNumbersInRadix32() {
        radix=32;
        //Character.MIN_RADIX;
        // Character.MAX_RADIX;
        BigInteger val31 = new BigInteger("31", 10);
        assertEquals("v", val31.toString(radix));

        BigInteger val0 = new BigInteger("0", 10);
        assertEquals("0", val0.toString(radix));

        BigInteger val15 = new BigInteger("15", 10);
        assertEquals("f", val15.toString(radix));

        BigInteger val16 = new BigInteger("16", 10);
        assertEquals("g", val16.toString(radix));
    }

    @Test
    public void printMaxNumberInRadix32Or16() {
        BigInteger maxValueInRadix10 = convertFromRadixTo10ByBigInteger(maxValueStr); //return number in format of  decimal radix.
        String maxValueInRadix32 = maxValueInRadix10.toString(radix);
        Double maxVal = Math.pow(radix, 11);
        long longMaxVal = maxVal.longValue() - 1;  // max value=32 ^ 11 -1;  because We start to count from zero not one
        assertEquals(String.valueOf(longMaxVal), maxValueInRadix10.toString(10)); //radix=32 -> 36,028,797,018,963,967 or radix=16 -> 17,433,288,880,143,855
    }

    @Test
    public void addOneToSomeRandomNumbers() {
        BigInteger maxValue = convertFromRadixTo10ByBigInteger(maxValueStr);
        Faker faker = Faker.instance();
        IntStream.range(0, 1000).forEach(i -> {
            long randomNumberIn10 = faker.random().nextLong(maxValue.longValue());
            long randomNumberIn10PlusOne = randomNumberIn10 + 1;
            String randomNumberIn32 = convertFrom10ToRadixByBigInteger(randomNumberIn10);
            assertEquals(11, randomNumberIn32.length());
            BigInteger tempNumberInRadix10 = convertFromRadixTo10ByBigInteger(randomNumberIn32);
            tempNumberInRadix10 = tempNumberInRadix10.add(BigInteger.valueOf(1L));
            assertEquals(tempNumberInRadix10.longValue(), randomNumberIn10PlusOne);
            //System.out.println("assert i="+i);
        });
    }

    @Test
    public void addOneToSomeRandomNumbersByUsingLongMethods() {
        long maxValue = convertFromRadixTo10ByLong(maxValueStr);
        Faker faker = Faker.instance();
        IntStream.range(0, 1000).forEach(i -> {
            long randomNumberIn10 = faker.random().nextLong(maxValue);
            long randomNumberIn10PlusOne = randomNumberIn10 + 1;
            String randomNumberIn32 = convertFrom10ToRadixByLong(randomNumberIn10);
            assertEquals(11, randomNumberIn32.length());
            long sum = Long.sum(convertFromRadixTo10ByLong(randomNumberIn32), 1L);
            assertEquals(sum, randomNumberIn10PlusOne);
          //  System.out.println("assert i=" + i);
        });
    }

//--------------------------------------------------------------  shared methods  ---------------------------------------------------------
    private BigInteger convertFromRadixTo10ByBigInteger(String numberIn32Or16) {
        return new BigInteger(numberIn32Or16, radix);
    }

    private long convertFromRadixTo10ByLong(String numberIn32Or16) {
        return Long.valueOf(numberIn32Or16, radix);
    }


    private String convertFrom10ToRadixByBigInteger(long numberIn10) {
        String str = new BigInteger(String.valueOf(numberIn10)).toString(radix);
        return fixedLengthString(str);
    }

    private String convertFrom10ToRadixByLong(long numberIn10) {

        String str = Long.toString(numberIn10, radix);
        return fixedLengthString(str);
    }


    private String fixedLengthString(String str) {
        StringBuilder stringBuilder = new StringBuilder(11);
        IntStream.range(0, 11 - str.length()).forEach(i -> {
            stringBuilder.append(0);
        });
        stringBuilder.append(str);
        return stringBuilder.toString();
    }
}
