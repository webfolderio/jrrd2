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
package org.opennms.netmgt.rrd.jrrd2.api;

import java.util.HashMap;
import java.util.Map;

/**
 * Used to store the results of a {@link org.opennms.netmgt.rrd.jrrd2.api.JRrd2#fetch}
 * or {@link org.opennms.netmgt.rrd.jrrd2.api.JRrd2#xport}.
 *
 * @author jwhite
 */
public class FetchResults {

    private final long m_start;

    private final long m_end;

    private final long m_step;

    private final String[] m_columns;

    private final double[][] m_values;

    public FetchResults(long start, long end, long step, String[] columns, double[][] values) {
        m_start = start;
        m_end = end;
        m_step = step;
        m_columns = columns;
        m_values = values;
    }

    public long getStart() {
        return m_start;
    }
    
    public long getEnd() {
        return m_end;
    }
    
    public long getStep() {
        return m_step;
    }

    public String[] getColumns() {
        return m_columns;
    }

    public double[][] getValues() {
        return m_values;
    }

    public long[] getTimestamps() {
        final int numRows = m_columns.length > 0 ? m_values[0].length : 0;
        final long[] timestamps = new long[numRows];
        for (int i = 0; i < numRows; i++) {
            timestamps[i] = m_start + m_step * i;
        }
        return timestamps;
    }

    public Map<String, double[]> getColumnsWithValues() {
        final Map<String, double[]> mappedValues = new HashMap<String, double[]>(m_columns.length);
        for (int i = 0; i < m_columns.length; i++) {
            mappedValues.put(m_columns[i], m_values[i]);
        }
        return mappedValues;
    }
}
