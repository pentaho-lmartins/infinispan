/*
 * JBoss, Home of Professional Open Source
 * Copyright 2009 Red Hat Inc. and/or its affiliates and other
 * contributors as indicated by the @author tags. All rights reserved.
 * See the copyright.txt in the distribution for a full listing of
 * individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.infinispan.marshall.exts;

import net.jcip.annotations.Immutable;

import org.infinispan.commons.io.UnsignedNumeric;
import org.infinispan.commons.marshall.AbstractExternalizer;
import org.infinispan.commons.marshall.MarshallUtil;
import org.infinispan.commons.util.Util;
import org.infinispan.marshall.core.Ids;
import org.jboss.marshalling.util.IdentityIntMap;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

/**
 * Set externalizer for all set implementations, i.e. HashSet and TreeSet
 *
 * @author Galder Zamarreño
 * @since 4.0
 */
@Immutable
public class SetExternalizer extends AbstractExternalizer<Set> {
   private static final int HASH_SET = 0;
   private static final int TREE_SET = 1;
   private final IdentityIntMap<Class<?>> numbers = new IdentityIntMap<Class<?>>(2);

   public SetExternalizer() {
      numbers.put(HashSet.class, HASH_SET);
      numbers.put(TreeSet.class, TREE_SET);
   }

   @Override
   public void writeObject(ObjectOutput output, Set set) throws IOException {
      int number = numbers.get(set.getClass(), -1);
      output.writeByte(number);
      if (number == TREE_SET)
         output.writeObject(((TreeSet) set).comparator());

      MarshallUtil.marshallCollection(set, output);
   }

   @Override
   public Set readObject(ObjectInput input) throws IOException, ClassNotFoundException {
      int magicNumber = input.readUnsignedByte();
      Set<Object> subject = null;
      switch (magicNumber) {
         case HASH_SET:
            subject = new HashSet();
            break;
         case TREE_SET:
            Comparator comparator = (Comparator) input.readObject();
            subject = new TreeSet(comparator);
            break;
      }
      int size = UnsignedNumeric.readUnsignedInt(input);
      for (int i = 0; i < size; i++) subject.add(input.readObject());
      return subject;
   }

   @Override
   public Integer getId() {
      return Ids.JDK_SETS;
   }

   @Override
   public Set<Class<? extends Set>> getTypeClasses() {
      return Util.<Class<? extends Set>>asSet(HashSet.class, TreeSet.class);
   }

}