package tr.edu.deu.efm.config;

import tr.edu.deu.efm.command.api.CommandRegistry;
import tr.edu.deu.efm.command.impl.*;
import tr.edu.deu.efm.core.api.*;
import tr.edu.deu.efm.core.impl.*;

public class RegistryConfig {
	
	/**
     * Don't let anyone instantiate this class.
     */
    private RegistryConfig() {
    	
    } 
	
    public static CommandRegistry initializeCommandRegistry() {
        CommandRegistry registry = new CommandRegistry();
        
        EntityRemover deleter;
        EntityMover mover;
        EntityCopier copier;
        EntityLister lister;
        EntityRenamer renamer;
        DirectoryChanger directoryChanger;
        
        if(Settings.removeSafely) {
        	deleter = new TrashBinRemover();
        } else {
        	deleter = new PermanentRemover();
        }
        
        if(Settings.overWriteMode) {
        	mover = new OverwriteMover();
        } else {
        	mover = new CopyMover();
        }
        
        copier = new DefaultCopier();
        lister = new DefaultLister();
        renamer = new DefaultRenamer();
        directoryChanger = new DefaultDirectoryChanger();
        
        registry.register("rm", new RmCommand(deleter));
        registry.register("mv", new MvCommand(mover));             
        registry.register("cp", new CpCommand(copier));
        
        registry.register("ls", new LsCommand(lister));
        registry.register("cd", new CdCommand(directoryChanger));
        registry.register("rn", new RnCommand(renamer));
        registry.register("exit", new ExitCommand());
        registry.register("help", new HelpCommand(registry));

        return registry;
    }
}	
