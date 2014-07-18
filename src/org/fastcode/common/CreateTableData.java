package org.fastcode.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreateTableData {
	private List<String>		schemasInDB							= new ArrayList<String>();
	private List<String>		stringColumnTypesList				= new ArrayList<String>();
	private List<String>		numericColumnTypesList				= new ArrayList<String>();
	private List<String>		dateTimeColumnTypesList				= new ArrayList<String>();
	private List<String>		othersColumnTypesList				= new ArrayList<String>();
	private boolean				createTableWithColumns;
	private boolean				addColumnsToExistingTable;
	private String				schemaSelected;
	private String				tableName;
	private String				columnTypeSelected;
	private String[]			columnNames;
	private List<String>		tablesInDB							= new ArrayList<String>();
	private int					columnTypeSize;
	private String				columnTypePrecisionAndScale;
	private Map<String, String>	dbTypeTableWithColumnsQueryMap		= new HashMap<String, String>();
	private Map<String, String>	dbTypeAddColumnsToExistingTableMap	= new HashMap<String, String>();
	private String				selectedDatabaseName;
	private String				notNull;
	private String				defaultValue;
	private String				dataType;
	private String				lenType;
	private boolean				getNewConnection;

	/**
	 * @return the schemasInDB
	 */
	public List<String> getSchemasInDB() {
		return this.schemasInDB;
	}

	/**
	 * @param schemasInDB the schemasInDB to set
	 */
	public void setSchemasInDB(final List<String> schemasInDB) {
		this.schemasInDB = schemasInDB;
	}

	/**
	 * @return the stringColumnTypesList
	 */
	public List<String> getStringColumnTypesList() {
		return this.stringColumnTypesList;
	}

	/**
	 * @param stringColumnTypesList the stringColumnTypesList to set
	 */
	public void setStringColumnTypesList(final List<String> stringColumnTypesList) {
		this.stringColumnTypesList = stringColumnTypesList;
	}

	/**
	 * @return the numericColumnTypesList
	 */
	public List<String> getNumericColumnTypesList() {
		return this.numericColumnTypesList;
	}

	/**
	 * @param numericColumnTypesList the numericColumnTypesList to set
	 */
	public void setNumericColumnTypesList(final List<String> numericColumnTypesList) {
		this.numericColumnTypesList = numericColumnTypesList;
	}

	/**
	 * @return the dateTimeColumnTypesList
	 */
	public List<String> getDateTimeColumnTypesList() {
		return this.dateTimeColumnTypesList;
	}

	/**
	 * @param dateTimeColumnTypesList the dateTimeColumnTypesList to set
	 */
	public void setDateTimeColumnTypesList(final List<String> dateTimeColumnTypesList) {
		this.dateTimeColumnTypesList = dateTimeColumnTypesList;
	}

	/**
	 * @return the othersColumnTypesList
	 */
	public List<String> getOthersColumnTypesList() {
		return this.othersColumnTypesList;
	}

	/**
	 * @param othersColumnTypesList the othersColumnTypesList to set
	 */
	public void setOthersColumnTypesList(final List<String> othersColumnTypesList) {
		this.othersColumnTypesList = othersColumnTypesList;
	}

	/**
	 * @return the createTableWithColumns
	 */
	public boolean isCreateTableWithColumns() {
		return this.createTableWithColumns;
	}

	/**
	 * @param createTableWithColumns the createTableWithColumns to set
	 */
	public void setCreateTableWithColumns(final boolean createTableWithColumns) {
		this.createTableWithColumns = createTableWithColumns;
	}

	/**
	 * @return the addColumnsToExistingTable
	 */
	public boolean isAddColumnsToExistingTable() {
		return this.addColumnsToExistingTable;
	}

	/**
	 * @param addColumnsToExistingTable the addColumnsToExistingTable to set
	 */
	public void setAddColumnsToExistingTable(final boolean addColumnsToExistingTable) {
		this.addColumnsToExistingTable = addColumnsToExistingTable;
	}

	/**
	 * @return the schemaSelected
	 */
	public String getSchemaSelected() {
		return this.schemaSelected;
	}

	/**
	 * @param schemaSelected the schemaSelected to set
	 */
	public void setSchemaSelected(final String schemaSelected) {
		this.schemaSelected = schemaSelected;
	}

	/**
	 * @return the tableName
	 */
	public String getTableName() {
		return this.tableName;
	}

	/**
	 * @param tableName the tableName to set
	 */
	public void setTableName(final String tableName) {
		this.tableName = tableName;
	}

	/**
	 * @return the columnTypeSelected
	 */
	public String getColumnTypeSelected() {
		return this.columnTypeSelected;
	}

	/**
	 * @param columnTypeSelected the columnTypeSelected to set
	 */
	public void setColumnTypeSelected(final String columnTypeSelected) {
		this.columnTypeSelected = columnTypeSelected;
	}

	/**
	 * @return the columnNames
	 */
	public String[] getColumnNames() {
		return this.columnNames;
	}

	/**
	 * @param columnNames the columnNames to set
	 */
	public void setColumnNames(final String[] columnNames) {
		this.columnNames = columnNames;
	}

	/**
	 * @return the tablesInDB
	 */
	public List<String> getTablesInDB() {
		return this.tablesInDB;
	}

	/**
	 * @param tablesInDB the tablesInDB to set
	 */
	public void setTablesInDB(final List<String> tablesInDB) {
		this.tablesInDB = tablesInDB;
	}

	/**
	 * @return the columnTypeSize
	 */
	public int getColumnTypeSize() {
		return this.columnTypeSize;
	}

	/**
	 * @param columnTypeSize the columnTypeSize to set
	 */
	public void setColumnTypeSize(final int columnTypeSize) {
		this.columnTypeSize = columnTypeSize;
	}

	/**
	 * @return the columnTypePrecisionAndScale
	 */
	public String getColumnTypePrecisionAndScale() {
		return this.columnTypePrecisionAndScale;
	}

	/**
	 * @param columnTypePrecisionAndScale the columnTypePrecisionAndScale to set
	 */
	public void setColumnTypePrecisionAndScale(final String columnTypePrecisionAndScale) {
		this.columnTypePrecisionAndScale = columnTypePrecisionAndScale;
	}

	/**
	 * @return the dbTypeTableWithColumnsQueryMap
	 */
	public Map<String, String> getDbTypeTableWithColumnsQueryMap() {
		return this.dbTypeTableWithColumnsQueryMap;
	}

	/**
	 * @param dbTypeTableWithColumnsQueryMap the dbTypeTableWithColumnsQueryMap to set
	 */
	public void setDbTypeTableWithColumnsQueryMap(final Map<String, String> dbTypeTableWithColumnsQueryMap) {
		this.dbTypeTableWithColumnsQueryMap = dbTypeTableWithColumnsQueryMap;
	}

	/**
	 * @return the dbTypeAddColumnsToExistingTableMap
	 */
	public Map<String, String> getDbTypeAddColumnsToExistingTableMap() {
		return this.dbTypeAddColumnsToExistingTableMap;
	}

	/**
	 * @param dbTypeAddColumnsToExistingTableMap the dbTypeAddColumnsToExistingTableMap to set
	 */
	public void setDbTypeAddColumnsToExistingTableMap(final Map<String, String> dbTypeAddColumnsToExistingTableMap) {
		this.dbTypeAddColumnsToExistingTableMap = dbTypeAddColumnsToExistingTableMap;
	}

	/**
	 * @return the selectedDatabaseName
	 */
	public String getSelectedDatabaseName() {
		return this.selectedDatabaseName;
	}

	/**
	 * @param selectedDatabaseName the selectedDatabaseName to set
	 */
	public void setSelectedDatabaseName(final String selectedDatabaseName) {
		this.selectedDatabaseName = selectedDatabaseName;
	}

	/**
	 *
	 * getter method for notNull
	 * @return
	 *
	 */
	public String getNotNull() {
		return this.notNull;
	}

	/**
	 *
	 * setter method for notNull
	 * @param notNull
	 *
	 */
	public void setNotNull(final String notNull) {
		this.notNull = notNull;
	}

	/**
	 *
	 * getter method for defaultValue
	 * @return
	 *
	 */
	public String getDefaultValue() {
		return this.defaultValue;
	}

	/**
	 *
	 * setter method for defaultValue
	 * @param defaultValue
	 *
	 */
	public void setDefaultValue(final String defaultValue) {
		this.defaultValue = defaultValue;
	}

	/**
	 *
	 * getter method for dataType
	 * @return
	 *
	 */
	public String getDataType() {
		return this.dataType;
	}

	/**
	 *
	 * setter method for dataType
	 * @param dataType
	 *
	 */
	public void setDataType(final String dataType) {
		this.dataType = dataType;
	}

	/**
	 *
	 * getter method for lenType
	 * @return
	 *
	 */
	public String getLenType() {
		return this.lenType;
	}

	/**
	 *
	 * setter method for lenType
	 * @param lenType
	 *
	 */
	public void setLenType(final String lenType) {
		this.lenType = lenType;
	}

	/**
	 *
	 * getter method for getNewConnection
	 * @return
	 *
	 */
	public boolean isGetNewConnection() {
		return this.getNewConnection;
	}

	/**
	 *
	 * setter method for getNewConnection
	 * @param getNewConnection
	 *
	 */
	public void setGetNewConnection(final boolean getNewConnection) {
		this.getNewConnection = getNewConnection;
	}

}
