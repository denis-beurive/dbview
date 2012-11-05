package tests;


import org.junit.Before;
import org.junit.Test;
import org.kohsuke.args4j.*;
import org.dbview.utils.args4j.*;

public class TestArgs4J
{    
    class CommandLineOptions extends OptionContainer
    {
        @Option(name="-a", aliases="--opta")
        @SuppressWarnings("unused")
        private boolean __opt_a;
        
        @Option(name="-b", aliases="--optb", handler=SpecialStringOptionHandler.class)
        @SuppressWarnings("unused")
        private String __opt_b;
    }
    
    @Before
    public void setUp()
    {
        System.out.println("");
        System.out.println("------------------------------------------------------------");
        System.out.println("Testing args4j.");
        System.out.println("------------------------------------------------------------");
        System.out.println("");
    }

    @Test public void tsstParsing1()
    {
        System.out.println("Nominal testing.");
        String[] argv = { "-a", "-b", "my string"};
        CommandLineOptions cliOpts = new CommandLineOptions();
        CmdLineParser parser = new CmdLineParser(cliOpts);
        
        try
        {
            parser.parseArgument(argv);
        }
        catch (CmdLineException e)
        {
            System.err.println(e.getMessage() + "\n");;
        }
        System.out.println(cliOpts);
        System.out.println("DONE");
    }
    
    @Test public void tsstParsing2()
    {
        System.out.println("Testing with an undefined option.");
        String[] argv = { "-a", "-b", "my string", "-c"};
        CommandLineOptions cliOpts = new CommandLineOptions();
        CmdLineParser parser = new CmdLineParser(cliOpts);
        
        try
        {
            parser.parseArgument(argv);
        }
        catch (CmdLineException e)
        {
            System.err.println(e.getMessage() + "\n");;
        }
        System.out.println(cliOpts);
        System.out.println("DONE");
    }   

    @Test public void tsstParsing3()
    {
        System.out.println("Testing wrong patterns.");
        String[] argv = { "-b", "-a"};
        CommandLineOptions cliOpts = new CommandLineOptions();
        CmdLineParser parser = new CmdLineParser(cliOpts);
        
        try
        {
            parser.parseArgument(argv);
        }
        catch (CmdLineException e)
        {
            System.err.println(e.getMessage() + "\n");;
        }
        System.out.println(cliOpts);
        System.out.println("DONE");
    }
    
    @Test public void tsstParsing4()
    {
        System.out.println("Testing missing value.");
        String[] argv = { "-b"};
        CommandLineOptions cliOpts = new CommandLineOptions();
        CmdLineParser parser = new CmdLineParser(cliOpts);
        
        try
        {
            parser.parseArgument(argv);
        }
        catch (CmdLineException e)
        {
            System.err.println(e.getMessage() + "\n");;
        }
        System.out.println(cliOpts);
        System.out.println("DONE");
    }
    
    @Test public void tsstParsing5()
    {
        System.out.println("Testing escaped value.");
        String[] argv = { "-b", "\\-thisIsNotAnOprion!"};
        CommandLineOptions cliOpts = new CommandLineOptions();
        CmdLineParser parser = new CmdLineParser(cliOpts);
        
        try
        {
            parser.parseArgument(argv);
        }
        catch (CmdLineException e)
        {
            System.err.println(e.getMessage() + "\n");;
        }
        System.out.println(cliOpts);
        System.out.println("DONE");
    } 
    
    @Test public void tsstParsing6()
    {
        System.out.println("Testing reflexion.");
        String[] argv = { "-b", "testing", "-a"};
        CommandLineOptions cliOpts = new CommandLineOptions();
        CmdLineParser parser = new CmdLineParser(cliOpts);
        
        try
        {
            parser.parseArgument(argv);
        }
        catch (CmdLineException e)
        {
            System.err.println(e.getMessage() + "\n");;
        }
        System.out.println(cliOpts);
        System.out.println("DONE");
    }
    
    @Test public void tsstParsing7()
    {
        System.out.println("Testing CLI selection.");
        String[] argv = { "-b", "testing", "-a", "-c", "-d"};
        CommandLineOptions cliOpts = new CommandLineOptions();
        CmdLineParser parser = new CmdLineParser(cliOpts);
        
        try
        {
            parser.parseArgument(cliOpts.extractArgvForThisSet(argv));
        }
        catch (CmdLineException e)
        {
            System.err.println(e.getMessage() + "\n");;
        }
        System.out.println(cliOpts);
        System.out.println("DONE");
    } 
    
    @Test public void tsstParsing8()
    {
        System.out.println("Testing CLI selection (should produce an error).");
        String[] argv = { "-a", "-b" };
        CommandLineOptions cliOpts = new CommandLineOptions();
        CmdLineParser parser = new CmdLineParser(cliOpts);
        
        try
        {
            parser.parseArgument(cliOpts.extractArgvForThisSet(argv));
        }
        catch (CmdLineException e)
        {
            System.err.println(e.getMessage() + "\n");;
        }
        System.out.println(cliOpts);
        System.out.println("DONE");
    } 
    
    @Test public void tsstParsing9()
    {
        System.out.println("Testing CLI selection.");
        String[] argv = { "-b", "testing", "-a", "-c", "-d", "-e"};
        CommandLineOptions cliOpts = new CommandLineOptions();
        CmdLineParser parser = new CmdLineParser(cliOpts);
        
        try
        {
            parser.parseArgument(cliOpts.extractArgvForThisSet(argv));
            for (String arg: cliOpts.shrink())
            {
                System.out.println("\t> " + arg);
            }
        }
        catch (CmdLineException e)
        {
            System.err.println(e.getMessage() + "\n");;
        }
        System.out.println(cliOpts);
        System.out.println("DONE");
    } 
}
