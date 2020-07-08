CREATE OR REPLACE EDITIONABLE FUNCTION "CLP_CONFIGURATION"."SF_UPDATEPRODUCTATTRIBUTE" (
    prod_id IN NUMBER,
	attribute_name IN varchar2, 
	attribute_value IN varchar2,
	attribute_group IN varchar2,
	operation IN varchar2) 

RETURN CLOB IS

	product_attributes       CLOB;
	product_attributes_obj   JSON_OBJECT_T;
	attribute_group_obj      JSON_OBJECT_T;
	result_attributes 		 CLOB := EMPTY_CLOB();
	curr_attribute_count	 NUMBER;
	new_attribute_count	     NUMBER;	
	UPDATE_FAILURE		     EXCEPTION;

PRAGMA EXCEPTION_INIT (UPDATE_FAILURE, -20100);
PRAGMA AUTONOMOUS_TRANSACTION;

BEGIN

		  dbms_output.put_line('Input prod_id: ' || prod_id || ' ,attribute_name: ' || 
		  attribute_name || ' ,attribute_value: ' || attribute_value || ' ,attribute_group: ' || 
		  attribute_group || ' ,operation: ' || operation);
		  -- Check inputs

	      IF (NVL(attribute_name, '') = '' OR NVL(attribute_group, '') = '' 
                OR NVL(operation, '') = '' OR prod_id <= 0) THEN
	          RAISE_APPLICATION_ERROR(-20100, 'Invalid input!');
	      END IF;

		  -- Load the attributes into a local CLOB.  
		  SELECT "ATTRIBUTES" INTO product_attributes FROM PRODUCT WHERE product_id = prod_id;

          IF (product_attributes IS NULL) THEN
            RAISE_APPLICATION_ERROR(-20100, 'Product attributes not found!');
          END IF;

		  -- Convert JSON CLOB to JSON object. 
		  product_attributes_obj := JSON_OBJECT_T.parse(product_attributes);

		  -- Get the attribute JSON object.
		  attribute_group_obj := treat(product_attributes_obj.get(attribute_group) as json_object_t);

          IF (attribute_group_obj IS NULL) THEN
            RAISE_APPLICATION_ERROR(-20100,'Attribute group not found!');
          END IF;

		  dbms_output.put_line('Found JSON fragment for group ' || attribute_group || ': ' || attribute_group_obj.to_string);

		  -- Get the current attribute count
		  curr_attribute_count := attribute_group_obj.get_keys().COUNT;		  

		  IF (UPPER(operation) = 'ADD') OR (UPPER(operation) = 'UPDATE') THEN

				attribute_group_obj.put(attribute_name, NVL(attribute_value,''));
				dbms_output.put_line('Updating ' || attribute_name || ' with value: ' || NVL(attribute_value,''));

		  ELSIF UPPER(operation) = 'DELETE' THEN

		   		dbms_output.put_line('Deleting ' || attribute_name);
			   	attribute_group_obj.remove(attribute_name);  

		  ELSIF UPPER(operation) = 'SELECT' THEN

		   		dbms_output.put_line('Retrieving ' || attribute_name);

		   		IF (attribute_group_obj.get(attribute_name) IS NULL) THEN
		   			return null;
		   		END IF;

			   	return attribute_group_obj.get(attribute_name).to_clob(); 			   	

		 ELSE 
		 		RAISE_APPLICATION_ERROR(-20100,'Operation ' || operation || ' not supported!');

		  END IF;

		  dbms_output.put_line('Updated JSON fragment for group ' || attribute_group || ': ' || attribute_group_obj.to_string);

		  -- Get new attribute count
		  new_attribute_count := attribute_group_obj.get_keys().COUNT;

		  dbms_output.put_line('curr_attribute_count: ' || curr_attribute_count || 
		  	' ,new_attribute_count: ' || new_attribute_count);

		  -- Make sure attribute counts match up
		  IF (UPPER(operation) = 'ADD' AND new_attribute_count < curr_attribute_count) THEN
				RAISE_APPLICATION_ERROR(-20100, 
				'New attribute count for ADD operation should be GREATER than current attribute count!');
		  END IF; 

		  IF (UPPER(operation) = 'UPDATE' AND new_attribute_count <> curr_attribute_count) THEN
				RAISE_APPLICATION_ERROR(-20100, 
				'New attribute count for UPDATE operation should be EQUAL to current attribute count!');	
		  END IF; 

		  IF (UPPER(operation) = 'DELETE' AND new_attribute_count > curr_attribute_count) THEN
				RAISE_APPLICATION_ERROR(-20100, 
				'New attribute count for DELETE operation should be LESS than current attribute count!');
		  END IF; 		  		

		  -- Convert the JSON object back to CLOB.
		  result_attributes := product_attributes_obj.to_clob();

          IF (result_attributes IS NULL OR 
            LENGTHB(TO_CHAR(SUBSTR(result_attributes,1,4000))) <= 0) THEN
				RAISE_APPLICATION_ERROR(-20100, 'Result Product attributes cannot be null!');
          END IF;          

          -- TODO move current PRODUCT record to Audit table

		  -- Update the PRODUCT table.
		  UPDATE PRODUCT SET "ATTRIBUTES" = result_attributes WHERE product_id = prod_id;

		  dbms_output.put_line('Committing Result');
		  COMMIT;

		  dbms_output.put_line('COMPLETED');

		  RETURN result_attributes;

EXCEPTION

		WHEN UPDATE_FAILURE THEN 
			DBMS_OUTPUT.PUT_LINE('Caught user defined error: ' || SQLCODE || ' ' || SQLERRM);
			RAISE_APPLICATION_ERROR(SQLCODE, SQLERRM);
			return null;
  		WHEN OTHERS THEN
    		DBMS_OUTPUT.PUT_LINE('Caught application error: ' || SQLCODE || ' ' || SQLERRM);
    		RAISE_APPLICATION_ERROR(SQLCODE, SQLERRM);
			return null;
END;

/
SHOW ERROR;