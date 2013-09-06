// System.java, created Fri Jan 11 17:08:41 2002 by joewhaley
// Copyright (C) 2001-3 John Whaley <jwhaley@alum.mit.edu>
// Licensed under the terms of the GNU LGPL; see COPYING for details.
package joeq.ClassLib.ibm13_linux.java.lang;

import joeq.Class.PrimordialClassLoader;

/**
 * System
 *
 * @author  John Whaley <jwhaley@alum.mit.edu>
 * @version $Id: System.java,v 1.8 2004/03/09 21:57:35 jwhaley Exp $
 */
public abstract class System {
    
    private static java.util.Properties initProperties(java.util.Properties props) {
        props.setProperty("java.version", "1.3.0");
        props.setProperty("java.vendor", "joeq");
        props.setProperty("java.vendor.url", "http://www.joewhaley.com");
        props.setProperty("java.class.version", "47.0");
        
        // TODO: read these properties from environment.
        props.setProperty("java.home", "/opt/IBMJava2-13/jre");
        props.setProperty("os.name", "Linux");
        props.setProperty("os.arch", "x86");
        props.setProperty("os.version", "2.4.9-31smp");
        props.setProperty("file.separator", "/");
        props.setProperty("path.separator", ":");
        props.setProperty("line.separator", "\n");
        props.setProperty("user.name", "jwhaley");
        props.setProperty("user.home", "/u/jwhaley");
        props.setProperty("user.dir", "/u/jwhaley/joeq");
        props.setProperty("java.class.path", PrimordialClassLoader.loader.classpathToString());
        return props;
    }
    
}
