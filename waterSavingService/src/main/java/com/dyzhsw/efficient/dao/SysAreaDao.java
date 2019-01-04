package com.dyzhsw.efficient.dao;

import com.dyzhsw.efficient.dto.SysAreaDTO;

import java.util.List;
import java.util.Map;

public interface SysAreaDao {
    List<SysAreaDTO> getAreas(Map map);
}
