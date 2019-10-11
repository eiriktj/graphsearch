package com.ntnu.app;

import java.util.ArrayList;
import java.util.List;

public class App
{
    public static void main( String[] args ) throws Exception
    {
        try ( ResultConsumeExample returnNodes = new ResultConsumeExample( "bolt://localhost:7687", "neo4j", "passord" ) )
        {
            List<String> nameList = returnNodes.getPeople();
            for(String element : nameList)
            {
                System.out.println(element);
            }
        }
    }
}
