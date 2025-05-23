/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.fury.meta;

import static org.apache.fury.meta.ClassDefEncoder.buildFieldsInfo;
import static org.apache.fury.meta.ClassDefEncoder.getClassFields;

import java.util.List;
import org.apache.fury.Fury;
import org.apache.fury.memory.MemoryBuffer;
import org.testng.Assert;
import org.testng.annotations.Test;

public class ClassDefEncoderTest {

  static class TestFieldsOrderClass1 {
    private int intField2;
    private boolean booleanField;
    private Object objField;
    private long longField;
  }

  @Test
  public void testBasicClassDef_zstdMetaCompressor() throws Exception {
    Fury fury =
        Fury.builder().withMetaShare(true).withMetaCompressor(new ZstdMetaCompressor()).build();
    Class<TestFieldsOrderClass1> type = TestFieldsOrderClass1.class;
    List<ClassDef.FieldInfo> fieldsInfo = buildFieldsInfo(fury.getClassResolver(), type);
    MemoryBuffer buffer =
        ClassDefEncoder.encodeClassDef(
            fury.getClassResolver(), type, getClassFields(type, fieldsInfo), true);
    ClassDef classDef = ClassDef.readClassDef(fury, buffer);
    Assert.assertEquals(classDef.getClassName(), type.getName());
    Assert.assertEquals(classDef.getFieldsInfo().size(), type.getDeclaredFields().length);
    Assert.assertEquals(classDef.getFieldsInfo(), fieldsInfo);
  }
}
