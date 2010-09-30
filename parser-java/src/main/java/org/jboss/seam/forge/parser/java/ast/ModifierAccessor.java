package org.jboss.seam.forge.parser.java.ast;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.Modifier.ModifierKeyword;

public class ModifierAccessor
{
   public boolean hasModifier(BodyDeclaration body, final ModifierKeyword modifier)
   {
      boolean result = false;
      List<Modifier> modifiers = getModifiers(body);
      for (Modifier m : modifiers)
      {
         if (m.getKeyword() == modifier)
         {
            result = true;
         }
      }
      return result;
   }

   private List<Modifier> getModifiers(BodyDeclaration body)
   {
      List<Modifier> result = new ArrayList<Modifier>();
      List<?> modifiers = body.modifiers();
      for (Object m : modifiers)
      {
         if (m instanceof Modifier)
         {
            Modifier mod = (Modifier) m;
            result.add(mod);
         }
      }
      return result;
   }

   @SuppressWarnings("unchecked")
   public List<Modifier> clearVisibility(BodyDeclaration body)
   {
      List<Modifier> modifiers = getModifiers(body);

      List<Modifier> toBeRemoved = new ArrayList<Modifier>();
      for (Modifier modifier : modifiers)
      {
         if (modifier.isPrivate() || modifier.isProtected() || modifier.isPublic())
         {
            toBeRemoved.add(modifier);
         }
      }

      body.modifiers().removeAll(toBeRemoved);
      return modifiers;
   }

   @SuppressWarnings("unchecked")
   public void addModifier(BodyDeclaration body, ModifierKeyword keyword)
   {
      body.modifiers().add(body.getAST().newModifier(keyword));
   }

   @SuppressWarnings("unchecked")
   public void removeModifier(BodyDeclaration body, ModifierKeyword keyword)
   {
      List<Modifier> modifiers = getModifiers(body);

      List<Modifier> toBeRemoved = new ArrayList<Modifier>();
      for (Modifier modifier : modifiers)
      {
         if (modifier.getKeyword().equals(keyword))
         {
            toBeRemoved.add(modifier);
         }
      }

      body.modifiers().removeAll(toBeRemoved);
   }
}
