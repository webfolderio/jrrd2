/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2010-2015 The OpenNMS Group, Inc.
 * OpenNMS(R) is Copyright (C) 1999-2015 The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is Copyright (C) 2002-2015 The OpenNMS Group, Inc.  All rights
 * reserved.  OpenNMS(R) is a derivative work, containing both original code,
 * included code and modified code that was published under the GNU General
 * Public License.  Copyrights for modified and included code are below.
 *
 * OpenNMS(R) is a registered trademark of The OpenNMS Group, Inc.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License with the Classpath
 * Exception; either version 2 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 *
 * For more information contact:
 *     OpenNMS(R) Licensing <license@opennms.org>
 *     http://www.opennms.org/
 *     http://www.opennms.com/
 *******************************************************************************/
package org.opennms.netmgt.rrd.jrrd2.impl;

import static java.lang.System.getProperty;
import static java.lang.System.load;
import static java.nio.file.Files.copy;
import static java.nio.file.Files.createDirectory;
import static java.nio.file.Files.createFile;
import static java.nio.file.Files.exists;
import static java.nio.file.Paths.get;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;

import org.opennms.netmgt.rrd.jrrd2.api.FetchResults;
import org.opennms.netmgt.rrd.jrrd2.api.JRrd2Exception;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A native interface to librrd.
 *
 * Use org.opennms.netmgt.rrd.jrrd2.impl.JRrd2 instead of making calls directly
 * to this interface.
 *
 * @author jwhite
 * @version 2.0.0
 */
public final class Interface {

    // temporary directory location
    private static final Path tmpdir = get(getProperty("java.io.tmpdir")).toAbsolutePath();

    private static final Logger LOG = LoggerFactory.getLogger(Interface.class);

    private static final String LIBRARY_NAME = "jrrd2";

    private static final String PROPERTY_NAME = "opennms.library.jrrd2";

    private static boolean m_loaded = false;

    private static final String version = "2.0.5";

    protected static native void rrd_get_context();

    protected static native void rrd_create_r(String filename, long pdp_step, long last_up, String[] argv)
            throws JRrd2Exception;

    protected static native void rrd_update_r(String filename, String template, String[] argv) throws JRrd2Exception;

    protected static native FetchResults rrd_fetch_r(String filename, String cf, long start, long end, long step)
            throws JRrd2Exception;

    protected static synchronized native FetchResults rrd_xport(String[] argv) throws JRrd2Exception;

    /**
     * Load the jrrd library and create the singleton instance of the interface.
     * 
     * @throws SecurityException    if we don't have permission to load the library
     * @throws UnsatisfiedLinkError if the library doesn't exist
     */
    public static synchronized void init() {
        if (m_loaded) {
            return;
        }
        Path libFile;
        ClassLoader cl = Interface.class.getClassLoader();
        try (InputStream is = cl.getResourceAsStream("META-INF/libjrrd2.so")) {
            libFile = tmpdir.resolve("jrrd2-" + version).resolve("libjrrd2.so");
            if (!exists(libFile.getParent())) {
                createDirectory(libFile.getParent());
            }
            if (!exists(libFile)) {
                createFile(libFile);
            }
            copy(is, libFile, REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        load(libFile.toString());
        m_loaded = true;
    }
}
