const { Project, SyntaxKind } = require('ts-morph');

const project = new Project({
  tsConfigFilePath: 'tsconfig.app.json'
});

const sourceFiles = project.getSourceFiles('src/app/features/**/*.ts');

for (const sourceFile of sourceFiles) {
  const classes = sourceFile.getClasses();
  let modified = false;

  for (const cls of classes) {
    if (!cls.getDecorator('Component')) continue;

    const subscribeCalls = sourceFile.getDescendantsOfKind(SyntaxKind.CallExpression)
      .filter(call => call.getExpression().getText().endsWith('.subscribe'));

    if (subscribeCalls.length === 0) continue;

    console.log(`Processing: ${sourceFile.getFilePath()}`);

    // Add imports
    const angularCoreImport = sourceFile.getImportDeclaration(dec => dec.getModuleSpecifierValue() === '@angular/core');
    if (angularCoreImport) {
      if (!angularCoreImport.getNamedImports().some(ni => ni.getName() === 'ChangeDetectorRef')) {
        angularCoreImport.addNamedImport('ChangeDetectorRef');
      }
      if (!angularCoreImport.getNamedImports().some(ni => ni.getName() === 'inject')) {
        angularCoreImport.addNamedImport('inject');
      }
    }

    // Check if cdr is already injected via constructor or property
    let hasCdr = false;
    for (const prop of cls.getProperties()) {
      if (prop.getType().getText() === 'ChangeDetectorRef') hasCdr = true;
    }
    for (const ctor of cls.getConstructors()) {
      for (const param of ctor.getParameters()) {
        if (param.getType().getText() === 'ChangeDetectorRef') hasCdr = true;
      }
    }

    if (!hasCdr) {
      cls.insertProperty(0, {
        name: 'cdr',
        scope: 'private',
        isReadonly: true,
        initializer: 'inject(ChangeDetectorRef)'
      });
      modified = true;
    }

    for (const call of subscribeCalls) {
      const args = call.getArguments();
      if (args.length === 1 && args[0].getKind() === SyntaxKind.ObjectLiteralExpression) {
        const obj = args[0];
        
        for (const prop of obj.getProperties()) {
          if (prop.getKind() === SyntaxKind.PropertyAssignment) {
            const name = prop.getName();
            if (name === 'next' || name === 'error') {
              const init = prop.getInitializer();
              if (init && init.getKind() === SyntaxKind.ArrowFunction) {
                const body = init.getBody();
                if (body.getKind() === SyntaxKind.Block) {
                  // Check if markForCheck is already there
                  const text = body.getText();
                  if (!text.includes('markForCheck()')) {
                    body.addStatements('this.cdr.markForCheck();');
                    modified = true;
                  }
                } else {
                  // Convert single expression to block
                  const oldBody = body.getText();
                  let params = init.getParameters().map(p => p.getText()).join(', ');
                  if (init.getParameters().length === 1 && !init.getText().startsWith('(')) {
                     // if it was just `arg => ...`
                     params = init.getParameters()[0].getText();
                  } else {
                     params = `(${params})`;
                  }
                  const newText = `{\n  ${oldBody};\n  this.cdr.markForCheck();\n}`;
                  init.replaceWithText(`${params} => ${newText}`);
                  modified = true;
                }
              }
            }
          }
        }
      }
    }
  }

  if (modified) {
    sourceFile.saveSync();
    console.log(`Saved: ${sourceFile.getFilePath()}`);
  }
}
